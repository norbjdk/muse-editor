import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function Upload() {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [uploadProgress, setUploadProgress] = useState(0);
    const [formData, setFormData] = useState({
        title: '',
        composer: '',
        difficulty: 'Intermediate',
        key: 'C Major',
        category: 'Original',
        description: '',
        tags: '',
        isPublic: true
    });
    const [file, setFile] = useState(null);
    const [fileError, setFileError] = useState('');
    const [submitSuccess, setSubmitSuccess] = useState(false);

    if (!user) {
        return (
            <div className="min-h-auto mt-15 py-8 bg-[#E8E4DD] pt-24 flex items-center justify-center px-4">
                <div className="text-center max-w-md">
                    <div className="w-24 h-24 mx-auto mb-6 bg-[#365603]/10 rounded-full flex items-center justify-center">
                        <span className="text-5xl">🎵</span>
                    </div>
                    <h2 className="text-2xl font-bold text-[#050505] mb-3">Please Log In</h2>
                    <p className="text-[#2c2c2c] mb-6">
                        You need to be logged in to upload sheet music. 
                        Sign in to share your compositions with the world.
                    </p>
                    <button
                        onClick={() => navigate('/login')}
                        className="px-6 py-2 bg-[#365603] text-[#f7f4ee] rounded-full hover:bg-[#213106] transition-all"
                    >
                        Go to Login
                    </button>
                </div>
            </div>
        );
    }

    const handleFileChange = (e) => {
        const selectedFile = e.target.files[0];
        setFileError('');
        
        if (!selectedFile) {
            setFile(null);
            return;
        }

        const validExtensions = ['.musicxml', '.mxl', '.xml'];
        const fileExtension = selectedFile.name.substring(selectedFile.name.lastIndexOf('.')).toLowerCase();
        
        if (!validExtensions.includes(fileExtension)) {
            setFileError('Please upload a valid MusicXML file (.musicxml, .mxl, or .xml)');
            setFile(null);
            return;
        }

        if (selectedFile.size > 10 * 1024 * 1024) {
            setFileError('File size must be less than 10MB');
            setFile(null);
            return;
        }

        setFile(selectedFile);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (!file) {
            setFileError('Please select a MusicXML file to upload');
            return;
        }

        if (!formData.title.trim()) {
            alert('Please enter a title');
            return;
        }

        setLoading(true);
        setUploadProgress(0);

        const formDataToSend = new FormData();
        formDataToSend.append('file', file);
        formDataToSend.append('title', formData.title);
        formDataToSend.append('composer', formData.composer);
        formDataToSend.append('difficulty', formData.difficulty);
        formDataToSend.append('key', formData.key);
        formDataToSend.append('category', formData.category);
        formDataToSend.append('description', formData.description);
        formDataToSend.append('tags', formData.tags);
        formDataToSend.append('isPublic', formData.isPublic);

        try {
            const response = await fetch('http://localhost:8080/api/sheets/upload', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: formDataToSend
            });

            const data = await response.json();
            
            if (data.success) {
                setUploadProgress(100);
                setSubmitSuccess(true);
                setTimeout(() => {
                    navigate('/profile');
                }, 2000);
            } else {
                throw new Error(data.message);
            }
        } catch (error) {
            console.error('Upload error:', error);
            alert('Failed to upload score. Please try again.');
            setUploadProgress(0);
        } finally {
            setLoading(false);
        }
    };

    if (submitSuccess) {
        return (
            <div className="min-h-screen bg-[#E8E4DD] pt-24 flex items-center justify-center px-4">
                <div className="text-center max-w-md">
                    <div className="w-24 h-24 mx-auto mb-6 bg-[#365603] rounded-full flex items-center justify-center">
                        <svg className="w-12 h-12 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                        </svg>
                    </div>
                    <h2 className="text-2xl font-bold text-[#050505] mb-3">Upload Successful!</h2>
                    <p className="text-[#2c2c2c] mb-6">
                        Your score "{formData.title}" has been published successfully.
                        Redirecting to your profile...
                    </p>
                    <div className="w-full bg-[#CCC5B9] rounded-full h-2 overflow-hidden">
                        <div className="bg-[#365603] h-2 rounded-full animate-pulse" style={{ width: '100%' }}></div>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-auto mt-15 py-6 bg-[#E8E4DD] pt-24 pb-12 px-4">
            <div className="max-w-4xl mx-auto">
                <div className="text-center mb-8">
                    <h1 className="text-3xl font-bold text-[#050505] mb-2">Upload Sheet Music</h1>
                    <p className="text-[#2c2c2c]">Share your compositions with the world</p>
                </div>

                <div className="bg-[#f7f4ee] rounded-2xl border border-[#CCC5B9] overflow-hidden">
                    <form onSubmit={handleSubmit} className="p-6 md:p-8">
                        <div className="space-y-6">
                            <div className="border-2 border-dashed border-[#CCC5B9] rounded-xl p-8 text-center hover:border-[#365603] transition-colors">
                                <input
                                    type="file"
                                    id="file-upload"
                                    accept=".musicxml,.mxl,.xml"
                                    onChange={handleFileChange}
                                    className="hidden"
                                    disabled={loading}
                                />
                                <label htmlFor="file-upload" className="cursor-pointer block">
                                    {file ? (
                                        <div>
                                            <div className="w-16 h-16 mx-auto mb-3 bg-[#365603]/10 rounded-full flex items-center justify-center">
                                                <svg className="w-8 h-8 text-[#365603]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                                                </svg>
                                            </div>
                                            <p className="text-[#050505] font-medium">{file.name}</p>
                                            <p className="text-sm text-[#2c2c2c] mt-1">
                                                {(file.size / 1024).toFixed(2)} KB
                                            </p>
                                            <p className="text-xs text-[#365603] mt-2">Click to change file</p>
                                        </div>
                                    ) : (
                                        <div>
                                            <div className="w-16 h-16 mx-auto mb-3 bg-[#E8E4DD] rounded-full flex items-center justify-center">
                                                <svg className="w-8 h-8 text-[#2c2c2c]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
                                                </svg>
                                            </div>
                                            <p className="text-[#050505] font-medium">Click to upload MusicXML file</p>
                                            <p className="text-sm text-[#2c2c2c] mt-1">Supported formats: .musicxml, .mxl, .xml</p>
                                            <p className="text-xs text-[#2c2c2c]">Maximum file size: 10MB</p>
                                        </div>
                                    )}
                                </label>
                                {fileError && (
                                    <p className="text-red-500 text-sm mt-3">{fileError}</p>
                                )}
                            </div>

                            <div className="grid md:grid-cols-2 gap-6">
                                <div>
                                    <label className="block text-sm font-medium text-[#050505] mb-2">Title *</label>
                                    <input
                                        type="text"
                                        required
                                        value={formData.title}
                                        onChange={(e) => setFormData({...formData, title: e.target.value})}
                                        className="w-full px-4 py-2 bg-[#E8E4DD] border border-[#CCC5B9] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#365603] text-[#050505]"
                                        placeholder="Enter composition title"
                                        disabled={loading}
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-[#050505] mb-2">Composer</label>
                                    <input
                                        type="text"
                                        value={formData.composer}
                                        onChange={(e) => setFormData({...formData, composer: e.target.value})}
                                        className="w-full px-4 py-2 bg-[#E8E4DD] border border-[#CCC5B9] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#365603] text-[#050505]"
                                        placeholder={user.username}
                                        disabled={loading}
                                    />
                                </div>
                            </div>

                            <div className="grid md:grid-cols-2 gap-6">
                                <div>
                                    <label className="block text-sm font-medium text-[#050505] mb-2">Difficulty</label>
                                    <select
                                        value={formData.difficulty}
                                        onChange={(e) => setFormData({...formData, difficulty: e.target.value})}
                                        className="w-full px-4 py-2 bg-[#E8E4DD] border border-[#CCC5B9] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#365603] text-[#050505]"
                                        disabled={loading}
                                    >
                                        <option value="Beginner">Beginner</option>
                                        <option value="Intermediate">Intermediate</option>
                                        <option value="Advanced">Advanced</option>
                                        <option value="Expert">Expert</option>
                                    </select>
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-[#050505] mb-2">Key</label>
                                    <select
                                        value={formData.key}
                                        onChange={(e) => setFormData({...formData, key: e.target.value})}
                                        className="w-full px-4 py-2 bg-[#E8E4DD] border border-[#CCC5B9] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#365603] text-[#050505]"
                                        disabled={loading}
                                    >
                                        <option>C Major</option>
                                        <option>C Minor</option>
                                        <option>G Major</option>
                                        <option>G Minor</option>
                                        <option>D Major</option>
                                        <option>D Minor</option>
                                        <option>A Major</option>
                                        <option>A Minor</option>
                                        <option>E Major</option>
                                        <option>E Minor</option>
                                        <option>F Major</option>
                                        <option>F Minor</option>
                                        <option>B♭ Major</option>
                                        <option>E♭ Major</option>
                                        <option>A♭ Major</option>
                                    </select>
                                </div>
                            </div>

                            <div className="grid md:grid-cols-2 gap-6">
                                <div>
                                    <label className="block text-sm font-medium text-[#050505] mb-2">Category</label>
                                    <select
                                        value={formData.category}
                                        onChange={(e) => setFormData({...formData, category: e.target.value})}
                                        className="w-full px-4 py-2 bg-[#E8E4DD] border border-[#CCC5B9] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#365603] text-[#050505]"
                                        disabled={loading}
                                    >
                                        <option value="Original">Original Composition</option>
                                        <option value="Arrangement">Arrangement</option>
                                        <option value="Transcription">Transcription</option>
                                        <option value="Public Domain">Public Domain</option>
                                    </select>
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-[#050505] mb-2">Tags</label>
                                    <input
                                        type="text"
                                        value={formData.tags}
                                        onChange={(e) => setFormData({...formData, tags: e.target.value})}
                                        className="w-full px-4 py-2 bg-[#E8E4DD] border border-[#CCC5B9] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#365603] text-[#050505]"
                                        placeholder="piano, classical, modern (comma separated)"
                                        disabled={loading}
                                    />
                                </div>
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-[#050505] mb-2">Description</label>
                                <textarea
                                    rows="4"
                                    value={formData.description}
                                    onChange={(e) => setFormData({...formData, description: e.target.value})}
                                    className="w-full px-4 py-2 bg-[#E8E4DD] border border-[#CCC5B9] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#365603] text-[#050505]"
                                    placeholder="Describe your composition, its inspiration, or any performance notes..."
                                    disabled={loading}
                                ></textarea>
                            </div>

                            <div className="flex items-center justify-between">
                                <label className="flex items-center cursor-pointer">
                                    <input
                                        type="checkbox"
                                        checked={formData.isPublic}
                                        onChange={(e) => setFormData({...formData, isPublic: e.target.checked})}
                                        className="w-4 h-4 text-[#365603] focus:ring-[#365603] border-[#CCC5B9] rounded"
                                        disabled={loading}
                                    />
                                    <span className="ml-2 text-sm text-[#050505]">Make this score public</span>
                                </label>
                                <p className="text-xs text-[#2c2c2c]">
                                    {formData.isPublic ? '✓ Everyone can view and download' : '🔒 Only you can see this score'}
                                </p>
                            </div>

                            {loading && (
                                <div className="space-y-2">
                                    <div className="flex justify-between text-sm text-[#2c2c2c]">
                                        <span>Uploading...</span>
                                        <span>{uploadProgress}%</span>
                                    </div>
                                    <div className="w-full bg-[#E8E4DD] rounded-full h-2 overflow-hidden">
                                        <div 
                                            className="bg-[#365603] h-2 rounded-full transition-all duration-300"
                                            style={{ width: `${uploadProgress}%` }}
                                        ></div>
                                    </div>
                                </div>
                            )}

                            <div className="flex gap-4 pt-4">
                                <button
                                    type="button"
                                    onClick={() => navigate('/profile')}
                                    className="flex-1 px-6 py-3 border border-[#CCC5B9] text-[#050505] rounded-full hover:bg-[#E8E4DD] transition-all font-semibold"
                                    disabled={loading}
                                >
                                    Cancel
                                </button>
                                <button
                                    type="submit"
                                    disabled={loading || !file}
                                    className="flex-1 px-6 py-3 bg-[#365603] text-[#f7f4ee] rounded-full hover:bg-[#213106] transition-all font-semibold disabled:opacity-50 disabled:cursor-not-allowed"
                                >
                                    {loading ? 'Uploading...' : 'Publish Score'}
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default Upload;
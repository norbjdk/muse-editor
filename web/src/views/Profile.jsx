import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { Link } from 'react-router-dom';

function Profile() {
    const { user } = useAuth();
    const [isEditing, setIsEditing] = useState(false);
    const [displayName, setDisplayName] = useState(user?.username || '');
    const [bio, setBio] = useState('Music lover and creator passionate about sharing art with the world.');

    if (!user) {
        return (
            <div className="min-h-auto mt-15 bg-[#E8E4DD] pt-24 flex items-center justify-center">
                <div className="text-center">
                    <h2 className="text-2xl font-bold text-[#050505] mb-4">Please log in</h2>
                    <Link to="/login" className="px-6 py-2 bg-[#365603] text-[#f7f4ee] rounded-full hover:bg-[#213106] transition-all">
                        Go to Login
                    </Link>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-auto mt-15 bg-[#E8E4DD] pt-24 pb-12 px-4">
            <div className="max-w-4xl mx-auto">
                <div className="bg-[#f7f4ee] rounded-2xl border border-[#CCC5B9] overflow-hidden">
                    <div className="h-32 bg-linear-to-r from-[#365603] to-[#213106]"></div>
                    
                    <div className="relative px-6 pb-6">
                        <div className="flex flex-col md:flex-row items-center md:items-end -mt-12 mb-6">
                            <div className="w-24 h-24 bg-[#365603] rounded-full flex items-center justify-center text-white text-3xl font-bold border-4 border-[#f7f4ee] shadow-lg">
                                {user.username?.charAt(0).toUpperCase()}
                            </div>
                            <div className="md:ml-4 mt-4 md:mt-0 text-center md:text-left flex-1">
                                {isEditing ? (
                                    <input
                                        type="text"
                                        value={displayName}
                                        onChange={(e) => setDisplayName(e.target.value)}
                                        className="text-2xl font-bold text-[#050505] bg-[#E8E4DD] border border-[#CCC5B9] rounded-lg px-3 py-1"
                                    />
                                ) : (
                                    <h1 className="text-2xl font-bold text-[#050505]">{displayName}</h1>
                                )}
                                <p className="text-[#2c2c2c] text-sm mt-1">
                                    {user.role === 'Artist' ? '🎤 Artist' : '🎧 Listener'}
                                </p>
                            </div>
                            <button
                                onClick={() => setIsEditing(!isEditing)}
                                className="mt-4 md:mt-0 px-4 py-2 text-sm border border-[#CCC5B9] rounded-lg text-[#050505] hover:bg-[#E8E4DD] transition-all"
                            >
                                {isEditing ? 'Save' : 'Edit Profile'}
                            </button>
                        </div>

                        <div className="border-t border-[#CCC5B9] pt-6">
                            <div className="mb-6">
                                <h3 className="text-sm font-semibold text-[#2c2c2c] uppercase tracking-wider mb-2">Bio</h3>
                                {isEditing ? (
                                    <textarea
                                        value={bio}
                                        onChange={(e) => setBio(e.target.value)}
                                        rows="4"
                                        className="w-full bg-[#E8E4DD] border border-[#CCC5B9] rounded-lg px-3 py-2 text-[#050505]"
                                    />
                                ) : (
                                    <p className="text-[#050505]">{bio}</p>
                                )}
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                <div>
                                    <h3 className="text-sm font-semibold text-[#2c2c2c] uppercase tracking-wider mb-2">Email</h3>
                                    <p className="text-[#050505]">{user.email}</p>
                                </div>
                                <div>
                                    <h3 className="text-sm font-semibold text-[#2c2c2c] uppercase tracking-wider mb-2">Member Since</h3>
                                    <p className="text-[#050505]">December 2024</p>
                                </div>
                            </div>

                            {user.role === 'Artist' && (
                                <div className="mt-6 p-4 bg-[#E8E4DD] rounded-lg">
                                    <h3 className="text-sm font-semibold text-[#2c2c2c] uppercase tracking-wider mb-3">Artist Stats</h3>
                                    <div className="grid grid-cols-3 gap-4 text-center">
                                        <div>
                                            <p className="text-2xl font-bold text-[#365603]">12</p>
                                            <p className="text-xs text-[#2c2c2c]">Scores</p>
                                        </div>
                                        <div>
                                            <p className="text-2xl font-bold text-[#365603]">2.4k</p>
                                            <p className="text-xs text-[#2c2c2c]">Downloads</p>
                                        </div>
                                        <div>
                                            <p className="text-2xl font-bold text-[#365603]">156</p>
                                            <p className="text-xs text-[#2c2c2c]">Favorites</p>
                                        </div>
                                    </div>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Profile;
import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

function AdminPanel() {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [activeTab, setActiveTab] = useState('users');
    const [users, setUsers] = useState([]);
    const [scores, setScores] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedUser, setSelectedUser] = useState(null);
    const [showUserModal, setShowUserModal] = useState(false);
    const [showDeleteConfirm, setShowDeleteConfirm] = useState(null);

    useEffect(() => {
        if (!user || user.role !== 'Admin') {
            navigate('/');
            return;
        }
        loadData();
    }, [user, navigate]);

    const loadData = () => {
        const mockUsers = [
            { id: 1, username: 'john_doe', email: 'john@example.com', role: 'Listener', status: 'active', joined: '2024-01-15', scoresCount: 3 },
            { id: 2, username: 'jane_smith', email: 'jane@example.com', role: 'Artist', status: 'active', joined: '2024-02-20', scoresCount: 12 },
            { id: 3, username: 'mike_artist', email: 'mike@example.com', role: 'Artist', status: 'suspended', joined: '2024-01-10', scoresCount: 8 },
            { id: 4, username: 'sarah_music', email: 'sarah@example.com', role: 'Listener', status: 'active', joined: '2024-03-05', scoresCount: 1 },
            { id: 5, username: 'tom_composer', email: 'tom@example.com', role: 'Artist', status: 'active', joined: '2024-01-25', scoresCount: 25 },
        ];

        const mockScores = [
            { id: 1, title: 'Nocturne Op. 9 No. 2', composer: 'Frédéric Chopin', uploader: 'jane_smith', difficulty: 'Advanced', downloads: 12400, status: 'published', uploadDate: '2024-02-15', category: 'Public Domain' },
            { id: 2, title: 'Moonlight Sonata', composer: 'Ludwig van Beethoven', uploader: 'mike_artist', difficulty: 'Intermediate', downloads: 25100, status: 'published', uploadDate: '2024-01-20', category: 'Public Domain' },
            { id: 3, title: 'Jazz Improvisation #1', composer: 'Jan Kowalski', uploader: 'tom_composer', difficulty: 'Advanced', downloads: 1200, status: 'pending', uploadDate: '2024-03-10', category: 'Original' },
            { id: 4, title: 'Modern Etude', composer: 'sarah_music', uploader: 'sarah_music', difficulty: 'Intermediate', downloads: 450, status: 'published', uploadDate: '2024-02-28', category: 'Original' },
            { id: 5, title: 'Questionable Score', composer: 'Unknown', uploader: 'john_doe', difficulty: 'Beginner', downloads: 89, status: 'reported', uploadDate: '2024-03-12', category: 'Arrangement' },
        ];

        setUsers(mockUsers);
        setScores(mockScores);
        setLoading(false);
    };

    if (!user || user.role !== 'Admin') {
        return null;
    }

    const filteredUsers = users.filter(u => 
        u.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
        u.email.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const filteredScores = scores.filter(s => 
        s.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        s.composer.toLowerCase().includes(searchTerm.toLowerCase()) ||
        s.uploader.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const handleUserRoleChange = (userId, newRole) => {
        setUsers(users.map(u => 
            u.id === userId ? { ...u, role: newRole } : u
        ));
        setShowUserModal(false);
    };

    const handleUserStatusChange = (userId, newStatus) => {
        setUsers(users.map(u => 
            u.id === userId ? { ...u, status: newStatus } : u
        ));
        setShowUserModal(false);
    };

    const handleDeleteUser = (userId) => {
        setUsers(users.filter(u => u.id !== userId));
        setShowDeleteConfirm(null);
    };

    const handleScoreStatusChange = (scoreId, newStatus) => {
        setScores(scores.map(s => 
            s.id === scoreId ? { ...s, status: newStatus } : s
        ));
    };

    const handleDeleteScore = (scoreId) => {
        setScores(scores.filter(s => s.id !== scoreId));
        setShowDeleteConfirm(null);
    };

    const getStatusBadge = (status) => {
        const badges = {
            active: 'bg-green-100 text-green-700',
            suspended: 'bg-red-100 text-red-700',
            pending: 'bg-yellow-100 text-yellow-700',
            published: 'bg-green-100 text-green-700',
            reported: 'bg-orange-100 text-orange-700'
        };
        return badges[status] || 'bg-gray-100 text-gray-700';
    };

    return (
        <div className="min-h-screen bg-[#E8E4DD] pt-24 pb-12 px-4">
            <div className="max-w-7xl mx-auto">
                <div className="mb-8">
                    <h1 className="text-3xl font-bold text-[#050505] mb-2">Admin Panel</h1>
                    <p className="text-[#2c2c2c]">Manage users, scores, and platform settings</p>
                </div>

                <div className="bg-[#f7f4ee] rounded-2xl border border-[#CCC5B9] overflow-hidden">
                    <div className="border-b border-[#CCC5B9]">
                        <div className="flex">
                            <button
                                onClick={() => setActiveTab('users')}
                                className={`px-6 py-3 font-semibold transition-all ${
                                    activeTab === 'users' 
                                        ? 'bg-[#365603] text-[#f7f4ee]' 
                                        : 'text-[#050505] hover:bg-[#E8E4DD]'
                                }`}
                            >
                                Users ({users.length})
                            </button>
                            <button
                                onClick={() => setActiveTab('scores')}
                                className={`px-6 py-3 font-semibold transition-all ${
                                    activeTab === 'scores' 
                                        ? 'bg-[#365603] text-[#f7f4ee]' 
                                        : 'text-[#050505] hover:bg-[#E8E4DD]'
                                }`}
                            >
                                Scores ({scores.length})
                            </button>
                            <button
                                onClick={() => setActiveTab('reports')}
                                className={`px-6 py-3 font-semibold transition-all ${
                                    activeTab === 'reports' 
                                        ? 'bg-[#365603] text-[#f7f4ee]' 
                                        : 'text-[#050505] hover:bg-[#E8E4DD]'
                                }`}
                            >
                                Reports (3)
                            </button>
                        </div>
                    </div>

                    <div className="p-6">
                        <div className="mb-6">
                            <div className="relative">
                                <svg className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-[#2c2c2c]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                                </svg>
                                <input
                                    type="text"
                                    placeholder={`Search ${activeTab}...`}
                                    value={searchTerm}
                                    onChange={(e) => setSearchTerm(e.target.value)}
                                    className="w-full pl-10 pr-4 py-2 bg-[#E8E4DD] border border-[#CCC5B9] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#365603] text-[#050505]"
                                />
                            </div>
                        </div>

                        {activeTab === 'users' && (
                            <div className="overflow-x-auto">
                                <table className="w-full">
                                    <thead className="bg-[#E8E4DD]">
                                        <tr className="text-left text-sm text-[#2c2c2c]">
                                            <th className="px-4 py-3">User</th>
                                            <th className="px-4 py-3">Email</th>
                                            <th className="px-4 py-3">Role</th>
                                            <th className="px-4 py-3">Status</th>
                                            <th className="px-4 py-3">Scores</th>
                                            <th className="px-4 py-3">Joined</th>
                                            <th className="px-4 py-3">Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {filteredUsers.map(user => (
                                            <tr key={user.id} className="border-t border-[#CCC5B9] hover:bg-[#E8E4DD]/50 transition-colors">
                                                <td className="px-4 py-3">
                                                    <div className="flex items-center gap-2">
                                                        <div className="w-8 h-8 bg-[#365603] rounded-full flex items-center justify-center text-white text-sm font-bold">
                                                            {user.username.charAt(0).toUpperCase()}
                                                        </div>
                                                        <span className="font-medium text-[#050505]">{user.username}</span>
                                                    </div>
                                                </td>
                                                <td className="px-4 py-3 text-[#2c2c2c]">{user.email}</td>
                                                <td className="px-4 py-3">
                                                    <span className={`px-2 py-1 rounded-full text-xs font-semibold ${
                                                        user.role === 'Admin' ? 'bg-purple-100 text-purple-700' :
                                                        user.role === 'Artist' ? 'bg-blue-100 text-blue-700' :
                                                        'bg-gray-100 text-gray-700'
                                                    }`}>
                                                        {user.role}
                                                    </span>
                                                </td>
                                                <td className="px-4 py-3">
                                                    <span className={`px-2 py-1 rounded-full text-xs font-semibold ${getStatusBadge(user.status)}`}>
                                                        {user.status}
                                                    </span>
                                                </td>
                                                <td className="px-4 py-3 text-[#2c2c2c]">{user.scoresCount}</td>
                                                <td className="px-4 py-3 text-[#2c2c2c] text-sm">{user.joined}</td>
                                                <td className="px-4 py-3">
                                                    <button
                                                        onClick={() => {
                                                            setSelectedUser(user);
                                                            setShowUserModal(true);
                                                        }}
                                                        className="text-[#365603] hover:text-[#213106] transition-colors"
                                                    >
                                                        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                                                        </svg>
                                                    </button>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                                {filteredUsers.length === 0 && (
                                    <div className="text-center py-8 text-[#2c2c2c]">No users found</div>
                                )}
                            </div>
                        )}

                        {activeTab === 'scores' && (
                            <div className="overflow-x-auto">
                                <table className="w-full">
                                    <thead className="bg-[#E8E4DD]">
                                        <tr className="text-left text-sm text-[#2c2c2c]">
                                            <th className="px-4 py-3">Title</th>
                                            <th className="px-4 py-3">Composer</th>
                                            <th className="px-4 py-3">Uploader</th>
                                            <th className="px-4 py-3">Difficulty</th>
                                            <th className="px-4 py-3">Downloads</th>
                                            <th className="px-4 py-3">Status</th>
                                            <th className="px-4 py-3">Date</th>
                                            <th className="px-4 py-3">Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {filteredScores.map(score => (
                                            <tr key={score.id} className="border-t border-[#CCC5B9] hover:bg-[#E8E4DD]/50 transition-colors">
                                                <td className="px-4 py-3 font-medium text-[#050505]">{score.title}</td>
                                                <td className="px-4 py-3 text-[#2c2c2c]">{score.composer}</td>
                                                <td className="px-4 py-3 text-[#2c2c2c] text-sm">{score.uploader}</td>
                                                <td className="px-4 py-3 text-[#2c2c2c] text-sm">{score.difficulty}</td>
                                                <td className="px-4 py-3 text-[#2c2c2c]">{score.downloads.toLocaleString()}</td>
                                                <td className="px-4 py-3">
                                                    <select
                                                        value={score.status}
                                                        onChange={(e) => handleScoreStatusChange(score.id, e.target.value)}
                                                        className={`px-2 py-1 rounded-full text-xs font-semibold border-0 ${getStatusBadge(score.status)}`}
                                                    >
                                                        <option value="published">Published</option>
                                                        <option value="pending">Pending</option>
                                                        <option value="reported">Reported</option>
                                                        <option value="hidden">Hidden</option>
                                                    </select>
                                                </td>
                                                <td className="px-4 py-3 text-[#2c2c2c] text-sm">{score.uploadDate}</td>
                                                <td className="px-4 py-3">
                                                    <div className="flex gap-2">
                                                        <button
                                                            onClick={() => navigate(`/score/${score.id}`)}
                                                            className="text-blue-600 hover:text-blue-800"
                                                        >
                                                            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                                                            </svg>
                                                        </button>
                                                        <button
                                                            onClick={() => setShowDeleteConfirm(score.id)}
                                                            className="text-red-600 hover:text-red-800"
                                                        >
                                                            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                                                            </svg>
                                                        </button>
                                                    </div>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                                {filteredScores.length === 0 && (
                                    <div className="text-center py-8 text-[#2c2c2c]">No scores found</div>
                                )}
                            </div>
                        )}

                        {activeTab === 'reports' && (
                            <div className="space-y-4">
                                <div className="bg-[#E8E4DD] rounded-lg p-4 border border-[#CCC5B9]">
                                    <div className="flex justify-between items-start mb-3">
                                        <div>
                                            <h3 className="font-semibold text-[#050505]">Inappropriate Content Report</h3>
                                            <p className="text-sm text-[#2c2c2c]">Reported by: john_doe • 2 hours ago</p>
                                        </div>
                                        <span className="px-2 py-1 bg-red-100 text-red-700 rounded-full text-xs font-semibold">Urgent</span>
                                    </div>
                                    <p className="text-[#2c2c2c] text-sm mb-3">Score: "Questionable Score" contains copyrighted material without permission.</p>
                                    <div className="flex gap-2">
                                        <button className="px-3 py-1 bg-red-600 text-white rounded-lg text-sm hover:bg-red-700 transition-colors">Remove Score</button>
                                        <button className="px-3 py-1 border border-[#CCC5B9] rounded-lg text-sm hover:bg-[#f7f4ee] transition-colors">Dismiss Report</button>
                                    </div>
                                </div>
                                <div className="bg-[#E8E4DD] rounded-lg p-4 border border-[#CCC5B9]">
                                    <div className="flex justify-between items-start mb-3">
                                        <div>
                                            <h3 className="font-semibold text-[#050505]">Wrong Category</h3>
                                            <p className="text-sm text-[#2c2c2c]">Reported by: mike_artist • 1 day ago</p>
                                        </div>
                                        <span className="px-2 py-1 bg-yellow-100 text-yellow-700 rounded-full text-xs font-semibold">Low</span>
                                    </div>
                                    <p className="text-[#2c2c2c] text-sm mb-3">Score "Modern Etude" is miscategorized as Original but it's an arrangement.</p>
                                    <div className="flex gap-2">
                                        <button className="px-3 py-1 bg-[#365603] text-white rounded-lg text-sm hover:bg-[#213106] transition-colors">Fix Category</button>
                                        <button className="px-3 py-1 border border-[#CCC5B9] rounded-lg text-sm hover:bg-[#f7f4ee] transition-colors">Dismiss</button>
                                    </div>
                                </div>
                            </div>
                        )}
                    </div>
                </div>

                <div className="mt-6 grid md:grid-cols-4 gap-4">
                    <div className="bg-[#f7f4ee] rounded-xl border border-[#CCC5B9] p-4 text-center">
                        <div className="text-2xl font-bold text-[#365603]">{users.length}</div>
                        <div className="text-sm text-[#2c2c2c]">Total Users</div>
                    </div>
                    <div className="bg-[#f7f4ee] rounded-xl border border-[#CCC5B9] p-4 text-center">
                        <div className="text-2xl font-bold text-[#365603]">{users.filter(u => u.role === 'Artist').length}</div>
                        <div className="text-sm text-[#2c2c2c]">Artists</div>
                    </div>
                    <div className="bg-[#f7f4ee] rounded-xl border border-[#CCC5B9] p-4 text-center">
                        <div className="text-2xl font-bold text-[#365603]">{scores.length}</div>
                        <div className="text-sm text-[#2c2c2c]">Total Scores</div>
                    </div>
                    <div className="bg-[#f7f4ee] rounded-xl border border-[#CCC5B9] p-4 text-center">
                        <div className="text-2xl font-bold text-[#365603]">{scores.reduce((sum, s) => sum + s.downloads, 0).toLocaleString()}</div>
                        <div className="text-sm text-[#2c2c2c]">Total Downloads</div>
                    </div>
                </div>
            </div>

            {showUserModal && selectedUser && (
                <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
                    <div className="bg-[#f7f4ee] rounded-2xl border border-[#CCC5B9] max-w-md w-full">
                        <div className="p-6">
                            <h2 className="text-xl font-bold text-[#050505] mb-4">Manage User: {selectedUser.username}</h2>
                            
                            <div className="space-y-4">
                                <div>
                                    <label className="block text-sm font-medium text-[#050505] mb-2">Role</label>
                                    <select
                                        value={selectedUser.role}
                                        onChange={(e) => handleUserRoleChange(selectedUser.id, e.target.value)}
                                        className="w-full px-4 py-2 bg-[#E8E4DD] border border-[#CCC5B9] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#365603]"
                                    >
                                        <option value="Listener">Listener</option>
                                        <option value="Artist">Artist</option>
                                        <option value="Admin">Admin</option>
                                    </select>
                                </div>
                                
                                <div>
                                    <label className="block text-sm font-medium text-[#050505] mb-2">Status</label>
                                    <select
                                        value={selectedUser.status}
                                        onChange={(e) => handleUserStatusChange(selectedUser.id, e.target.value)}
                                        className="w-full px-4 py-2 bg-[#E8E4DD] border border-[#CCC5B9] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#365603]"
                                    >
                                        <option value="active">Active</option>
                                        <option value="suspended">Suspended</option>
                                    </select>
                                </div>

                                <div className="pt-4 flex gap-3">
                                    <button
                                        onClick={() => setShowUserModal(false)}
                                        className="flex-1 px-4 py-2 border border-[#CCC5B9] rounded-lg hover:bg-[#E8E4DD] transition-colors"
                                    >
                                        Close
                                    </button>
                                    <button
                                        onClick={() => {
                                            setShowDeleteConfirm(selectedUser.id);
                                            setShowUserModal(false);
                                        }}
                                        className="flex-1 px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors"
                                    >
                                        Delete User
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            )}

            {showDeleteConfirm && (
                <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
                    <div className="bg-[#f7f4ee] rounded-2xl border border-[#CCC5B9] max-w-md w-full p-6">
                        <div className="text-center">
                            <div className="w-16 h-16 mx-auto mb-4 bg-red-100 rounded-full flex items-center justify-center">
                                <svg className="w-8 h-8 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
                                </svg>
                            </div>
                            <h3 className="text-lg font-bold text-[#050505] mb-2">Confirm Delete</h3>
                            <p className="text-[#2c2c2c] mb-6">
                                Are you sure you want to delete this {activeTab === 'users' ? 'user' : 'score'}? 
                                This action cannot be undone.
                            </p>
                            <div className="flex gap-3">
                                <button
                                    onClick={() => setShowDeleteConfirm(null)}
                                    className="flex-1 px-4 py-2 border border-[#CCC5B9] rounded-lg hover:bg-[#E8E4DD] transition-colors"
                                >
                                    Cancel
                                </button>
                                <button
                                    onClick={() => {
                                        if (activeTab === 'users') {
                                            handleDeleteUser(showDeleteConfirm);
                                        } else {
                                            handleDeleteScore(showDeleteConfirm);
                                        }
                                    }}
                                    className="flex-1 px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors"
                                >
                                    Delete
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default AdminPanel;
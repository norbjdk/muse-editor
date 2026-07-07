import { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';
import { userAPI, projectsAPI } from '../services/api';

function Tab({ active, onClick, children }) {
  return (
    <button
      onClick={onClick}
      className={`px-4 py-2 text-sm font-semibold tracking-wide transition-all duration-150 border-b-2 ${
        active
          ? 'border-slate-800 text-slate-800'
          : 'border-transparent text-stone-500 hover:text-slate-700'
      }`}
    >
      {children}
    </button>
  );
}

function Badge({ children, color = 'stone' }) {
  const colors = {
    stone:   'bg-stone-100 text-stone-600',
    amber:   'bg-amber-100 text-amber-700',
    red:     'bg-red-100 text-red-700',
    emerald: 'bg-emerald-100 text-emerald-700',
    slate:   'bg-slate-100 text-slate-700',
  };
  return (
    <span className={`px-2 py-0.5 rounded-full text-xs font-semibold uppercase tracking-wide ${colors[color]}`}>
      {children}
    </span>
  );
}

function ConfirmButton({ onConfirm, label, confirmLabel = 'Sure?' }) {
  const [confirming, setConfirming] = useState(false);
  return confirming ? (
    <span className="flex gap-1">
      <button
        onClick={() => { setConfirming(false); onConfirm(); }}
        className="text-xs px-2 py-1 bg-red-600 text-white rounded-lg font-medium"
      >
        {confirmLabel}
      </button>
      <button
        onClick={() => setConfirming(false)}
        className="text-xs px-2 py-1 bg-stone-200 text-stone-600 rounded-lg font-medium"
      >
        Cancel
      </button>
    </span>
  ) : (
    <button
      onClick={() => setConfirming(true)}
      className="text-xs px-3 py-1.5 bg-red-900/10 hover:bg-red-900/20 text-red-700 rounded-lg font-medium transition-colors"
    >
      {label}
    </button>
  );
}

function Admin() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [tab, setTab] = useState('users');

  const [users, setUsers] = useState([]);
  const [usersLoading, setUsersLoading] = useState(true);
  const [usersError, setUsersError] = useState('');

  const [projects, setProjects] = useState([]);
  const [projectsLoading, setProjectsLoading] = useState(true);
  const [projectsError, setProjectsError] = useState('');

  const [search, setSearch] = useState('');

  useEffect(() => {
    if (user && user.role !== 'ADMIN') navigate('/');
  }, [user]);

  useEffect(() => {
    if (tab === 'users') loadUsers();
    if (tab === 'scores') loadProjects();
  }, [tab]);

  const loadUsers = async () => {
    setUsersLoading(true);
    setUsersError('');
    try {
      const res = await userAPI.getAllUsers();
      setUsers(res.data || []);
    } catch {
      setUsersError('Failed to load users.');
    } finally {
      setUsersLoading(false);
    }
  };

  const loadProjects = async () => {
    setProjectsLoading(true);
    setProjectsError('');
    try {
      const res = await projectsAPI.getAllProjects();
      setProjects(res.data || []);
    } catch {
      setProjectsError('Failed to load scores.');
    } finally {
      setProjectsLoading(false);
    }
  };

  const handleDeleteUser = async (id) => {
    try {
      await userAPI.deleteUser(id);
      setUsers((prev) => prev.filter((u) => u.id !== id));
    } catch {
      alert('Failed to delete user.');
    }
  };

  const handleDeleteProject = async (id) => {
    try {
      await projectsAPI.deleteProject(id);
      setProjects((prev) => prev.filter((p) => p.id !== id));
    } catch {
      alert('Failed to delete score.');
    }
  };

  const handleToggleRole = async (u) => {
    const newRole = u.role === 'ADMIN' ? 'USER' : 'ADMIN';
    try {
      await userAPI.updateUserRole(u.id, newRole);
      setUsers((prev) => prev.map((x) => x.id === u.id ? { ...x, role: newRole } : x));
    } catch {
      alert('Failed to update role.');
    }
  };

  const filteredUsers = users.filter((u) =>
    u.username?.toLowerCase().includes(search.toLowerCase()) ||
    u.email?.toLowerCase().includes(search.toLowerCase())
  );

  const filteredProjects = projects.filter((p) =>
    p.title?.toLowerCase().includes(search.toLowerCase()) ||
    p.creator?.toLowerCase().includes(search.toLowerCase())
  );

  if (!user) return null;

  return (
    <div className="p-6 sm:p-8 max-w-5xl mx-auto w-full">
      <div className="shadow-xl rounded-4xl overflow-hidden border border-stone-300/50">

        {/* Header */}
        <div className="bg-linear-to-r from-slate-800 via-slate-700 to-slate-800 px-6 py-5 flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-extrabold tracking-tight text-amber-50">Admin Panel</h1>
            <p className="text-xs text-stone-400 mt-0.5">Manage users and music scores</p>
          </div>
          <div className="flex items-center gap-3">
            <span className="text-xs text-stone-400">{user.username}</span>
            <Badge color="amber">Admin</Badge>
          </div>
        </div>

        {/* Tabs + search */}
        <div className="bg-white/20 border border-[#CCC5B9]/90">
          <div className="flex items-center justify-between px-6 pt-4 border-b border-stone-300/50 gap-4 flex-wrap">
            <div className="flex gap-0">
              <Tab active={tab === 'users'} onClick={() => { setTab('users'); setSearch(''); }}>
                Users {!usersLoading && <span className="ml-1 text-stone-400 font-normal">({users.length})</span>}
              </Tab>
              <Tab active={tab === 'scores'} onClick={() => { setTab('scores'); setSearch(''); }}>
                Scores {!projectsLoading && <span className="ml-1 text-stone-400 font-normal">({projects.length})</span>}
              </Tab>
            </div>
            <input
              type="text"
              placeholder="Search..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="bg-amber-50/60 border border-stone-300 rounded-xl px-4 py-1.5 text-stone-800 placeholder-stone-400 outline-hidden focus:bg-amber-50 focus:border-stone-500 focus:ring-2 focus:ring-stone-500/20 transition-all text-sm mb-3 w-52"
            />
          </div>

          <div className="px-6 py-6">

            {/* USERS TAB */}
            {tab === 'users' && (
              <>
                {usersLoading ? (
                  <div className="flex items-center gap-2 text-stone-500 text-sm py-8 justify-center">
                    <div className="h-2 w-2 rounded-full bg-amber-400 animate-pulse" />
                    Loading users...
                  </div>
                ) : usersError ? (
                  <p className="text-sm text-red-600 py-4">{usersError}</p>
                ) : filteredUsers.length === 0 ? (
                  <p className="text-sm text-stone-500 py-8 text-center italic">No users found.</p>
                ) : (
                  <div className="flex flex-col gap-2">
                    {/* Table header */}
                    <div className="hidden sm:grid grid-cols-[1fr_1.5fr_auto_auto_auto] gap-4 px-4 py-2 text-xs font-semibold tracking-widest text-stone-500 uppercase">
                      <span>Username</span>
                      <span>Email</span>
                      <span>Role</span>
                      <span>Scores</span>
                      <span></span>
                    </div>
                    {filteredUsers.map((u) => (
                      <div
                        key={u.id}
                        className="grid grid-cols-1 sm:grid-cols-[1fr_1.5fr_auto_auto_auto] gap-2 sm:gap-4 items-center px-4 py-3 bg-white/30 rounded-xl border border-stone-300/40 hover:bg-white/40 transition-colors"
                      >
                        <span className="text-sm font-semibold text-slate-800 truncate">
                          {u.username}
                        </span>
                        <span className="text-xs text-stone-500 truncate">{u.email || '—'}</span>
                        <Badge color={u.role === 'ADMIN' ? 'amber' : 'stone'}>
                          {u.role || 'user'}
                        </Badge>
                        <span className="text-xs text-stone-500 font-medium">{u.projectCount ?? '—'}</span>
                        <div className="flex items-center gap-1.5 flex-wrap">
                          <button
                            onClick={() => handleToggleRole(u)}
                            className="text-xs px-3 py-1.5 bg-slate-800/10 hover:bg-slate-800/20 text-slate-700 rounded-lg font-medium transition-colors"
                          >
                            {u.role === 'ADMIN' ? 'Demote' : 'Make Admin'}
                          </button>
                          <ConfirmButton
                            onConfirm={() => handleDeleteUser(u.id)}
                            label="Delete"
                          />
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </>
            )}

            {/* SCORES TAB */}
            {tab === 'scores' && (
              <>
                {projectsLoading ? (
                  <div className="flex items-center gap-2 text-stone-500 text-sm py-8 justify-center">
                    <div className="h-2 w-2 rounded-full bg-amber-400 animate-pulse" />
                    Loading scores...
                  </div>
                ) : projectsError ? (
                  <p className="text-sm text-red-600 py-4">{projectsError}</p>
                ) : filteredProjects.length === 0 ? (
                  <p className="text-sm text-stone-500 py-8 text-center italic">No scores found.</p>
                ) : (
                  <div className="flex flex-col gap-2">
                    <div className="hidden sm:grid grid-cols-[1fr_1fr_auto_auto_auto] gap-4 px-4 py-2 text-xs font-semibold tracking-widest text-stone-500 uppercase">
                      <span>Title</span>
                      <span>Creator</span>
                      <span>Owner</span>
                      <span>Status</span>
                      <span></span>
                    </div>
                    {filteredProjects.map((p) => (
                      <div
                        key={p.id}
                        className="grid grid-cols-1 sm:grid-cols-[1fr_1fr_auto_auto_auto] gap-2 sm:gap-4 items-center px-4 py-3 bg-white/30 rounded-xl border border-stone-300/40 hover:bg-white/40 transition-colors"
                      >
                        <span className="text-sm font-semibold text-slate-800 truncate">
                          {p.title || 'Untitled'}
                        </span>
                        <span className="text-xs text-stone-500 truncate">{p.creator || '—'}</span>
                        <span className="text-xs text-stone-500 truncate">{p.ownerUsername || '—'}</span>
                        <Badge color={p.published ? 'emerald' : 'stone'}>
                          {p.published ? 'Published' : 'Draft'}
                        </Badge>
                        <ConfirmButton
                          onConfirm={() => handleDeleteProject(p.id)}
                          label="Delete"
                        />
                      </div>
                    ))}
                  </div>
                )}
              </>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default Admin;

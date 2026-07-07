import { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { socialsAPI, projectsAPI } from '../services/api';

const SOCIAL_FIELDS = [
  { key: 'youtubeId',     label: 'YouTube',   placeholder: 'channel handle' },
  { key: 'spotifyId',     label: 'Spotify',   placeholder: 'artist ID' },
  { key: 'instagramId',   label: 'Instagram', placeholder: 'username' },
  { key: 'tiktokId',      label: 'TikTok',    placeholder: 'username' },
  { key: 'personalWebUrl',label: 'Website',   placeholder: 'https://...' },
];

function getInitials(username) {
  if (!username) return '?';
  return username.slice(0, 2).toUpperCase();
}

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

function Profile() {
  const { user, fetchCurrentUser } = useAuth();
  const [tab, setTab] = useState('scores');

  const [socials, setSocials] = useState({});
  const [socialsLoading, setSocialsLoading] = useState(false);
  const [socialsSaved, setSocialsSaved] = useState(false);
  const [socialsError, setSocialsError] = useState('');

  const [projects, setProjects] = useState([]);
  const [projectsLoading, setProjectsLoading] = useState(true);

  useEffect(() => {
    if (tab === 'socials') loadSocials();
    if (tab === 'scores') loadProjects();
  }, [tab]);

  const loadSocials = async () => {
    setSocialsLoading(true);
    try {
      const res = await socialsAPI.getSocials();
      setSocials(res.data || {});
    } catch {
      setSocials({});
    } finally {
      setSocialsLoading(false);
    }
  };

  const loadProjects = async () => {
    setProjectsLoading(true);
    try {
      const res = await projectsAPI.getMyProjects();
      setProjects(res.data || []);
    } catch {
      setProjects([]);
    } finally {
      setProjectsLoading(false);
    }
  };

  const handleSaveSocials = async (e) => {
    e.preventDefault();
    setSocialsError('');
    setSocialsSaved(false);
    try {
      await socialsAPI.updateSocials(socials);
      setSocialsSaved(true);
      setTimeout(() => setSocialsSaved(false), 2500);
    } catch {
      setSocialsError('Failed to save. Try again.');
    }
  };

  const handleDeleteProject = async (id) => {
    if (!confirm('Remove this score?')) return;
    try {
      await projectsAPI.deleteProject(id);
      setProjects((prev) => prev.filter((p) => p.id !== id));
    } catch {
      alert('Failed to delete.');
    }
  };

  if (!user) return null;

  return (
    <div className="flex flex-col sm:flex-row gap-6 p-6 sm:p-8 items-start justify-center">

      {/* Left — identity card */}
      <div className="w-full sm:w-72 flex-shrink-0">
        <div className="shadow-xl rounded-4xl overflow-hidden border border-stone-300/50">
          <div className="bg-linear-to-r from-slate-800 via-slate-700 to-slate-800 px-6 py-4 flex justify-between items-center">
            <h2 className="text-xl font-extrabold tracking-tight text-amber-50">My Profile</h2>
            <div className="h-2 w-2 rounded-full bg-amber-400 animate-pulse" />
          </div>
          <div className="bg-white/20 border border-[#CCC5B9]/90 px-6 py-6 flex flex-col gap-4">
            <div className="flex flex-col items-center gap-3 pb-4 border-b border-stone-400/30">
              <div className="w-20 h-20 rounded-full bg-slate-700 border-2 border-amber-400/60 flex items-center justify-center text-amber-50 text-2xl font-extrabold select-none">
                {getInitials(user.username)}
              </div>
              <div className="text-center">
                <p className="text-lg font-extrabold tracking-tight text-slate-800">{user.username}</p>
                {user.email && (
                  <p className="text-xs text-stone-500 mt-0.5">{user.email}</p>
                )}
                <span className="mt-1 inline-block px-2 py-0.5 bg-slate-800/10 rounded-full text-xs font-semibold text-slate-600 uppercase tracking-widest">
                  {user.role || 'user'}
                </span>
              </div>
            </div>

            <div className="flex flex-col gap-1.5">
              {[
                ['Scores', projects.length],
                ['Member since', user.createdAt ? new Date(user.createdAt).toLocaleDateString('pl-PL', { year: 'numeric', month: 'short' }) : '—'],
              ].map(([label, val]) => (
                <div key={label} className="flex justify-between items-center py-2 px-3 bg-white/30 rounded-xl border border-stone-300/40 text-sm">
                  <span className="text-stone-500 font-medium">{label}</span>
                  <span className="text-slate-800 font-semibold">{val}</span>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>

      {/* Right — tabs */}
      <div className="w-full flex-1 min-w-0">
        <div className="shadow-xl rounded-4xl overflow-hidden border border-stone-300/50">
          <div className="bg-linear-to-r from-slate-800 via-slate-700 to-slate-800 px-6 py-4">
            <h2 className="text-xl font-extrabold tracking-tight text-amber-50">Settings</h2>
          </div>
          <div className="bg-white/20 border border-[#CCC5B9]/90">
            <div className="flex gap-0 border-b border-stone-300/50 px-6 pt-4">
              <Tab active={tab === 'scores'} onClick={() => setTab('scores')}>Music Scores</Tab>
              <Tab active={tab === 'socials'} onClick={() => setTab('socials')}>Social Media</Tab>
            </div>

            <div className="px-6 py-6">
              {/* SCORES TAB */}
              {tab === 'scores' && (
                <div>
                  {projectsLoading ? (
                    <div className="flex items-center gap-2 text-stone-500 text-sm py-8 justify-center">
                      <div className="h-2 w-2 rounded-full bg-amber-400 animate-pulse" />
                      Loading scores...
                    </div>
                  ) : projects.length === 0 ? (
                    <div className="flex flex-col items-center justify-center py-16 gap-3">
                      <span className="text-5xl opacity-20 select-none">𝄽</span>
                      <p className="text-stone-500 text-sm italic">No published scores yet.</p>
                    </div>
                  ) : (
                    <div className="flex flex-col gap-2">
                      {projects.map((p) => (
                        <div
                          key={p.id}
                          className="flex items-center justify-between px-4 py-3 bg-white/30 rounded-xl border border-stone-300/40 hover:bg-white/40 transition-colors"
                        >
                          <div className="min-w-0">
                            <p className="text-sm font-semibold text-slate-800 truncate">{p.title || 'Untitled'}</p>
                            <p className="text-xs text-stone-500 mt-0.5">{p.creator || '—'}</p>
                          </div>
                          <div className="flex items-center gap-2 ml-4 flex-shrink-0">
                            <span className={`text-xs px-2 py-0.5 rounded-full font-semibold ${p.published ? 'bg-emerald-100 text-emerald-700' : 'bg-stone-100 text-stone-500'}`}>
                              {p.published ? 'Published' : 'Draft'}
                            </span>
                            <button
                              onClick={() => handleDeleteProject(p.id)}
                              className="text-xs px-3 py-1.5 bg-red-900/10 hover:bg-red-900/20 text-red-700 rounded-lg font-medium transition-colors"
                            >
                              Remove
                            </button>
                          </div>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              )}

              {/* SOCIALS TAB */}
              {tab === 'socials' && (
                <form onSubmit={handleSaveSocials} className="flex flex-col gap-4">
                  {socialsLoading ? (
                    <div className="py-8 flex justify-center text-stone-500 text-sm">Loading...</div>
                  ) : (
                    <>
                      {SOCIAL_FIELDS.map(({ key, label, placeholder }) => (
                        <div key={key} className="flex flex-col gap-1.5">
                          <label className="text-sm font-semibold text-stone-700 tracking-wide">{label}</label>
                          <input
                            type="text"
                            placeholder={placeholder}
                            value={socials[key] || ''}
                            onChange={(e) => setSocials((prev) => ({ ...prev, [key]: e.target.value }))}
                            className="w-full bg-amber-50/60 border border-stone-300 rounded-xl px-4 py-2.5 text-stone-800 placeholder-stone-400 outline-hidden focus:bg-amber-50 focus:border-stone-500 focus:ring-3 focus:ring-stone-500/20 transition-all duration-200"
                          />
                        </div>
                      ))}

                      {socialsError && (
                        <p className="text-sm text-red-600">{socialsError}</p>
                      )}

                      <div className="flex items-center gap-3 mt-2">
                        <button
                          type="submit"
                          className="bg-slate-800 hover:bg-stone-900 text-amber-50 font-medium px-6 py-2.5 rounded-xl transition-all duration-150 text-sm"
                        >
                          Save
                        </button>
                        {socialsSaved && (
                          <span className="text-sm text-emerald-600 font-medium">Saved.</span>
                        )}
                      </div>
                    </>
                  )}
                </form>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Profile;

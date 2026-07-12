import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { userAPI, projectsAPI } from '../services/api';

const SOCIAL_LINKS = [
  {
    key: 'youtubeId',
    label: 'YouTube',
    href: (id) => `https://youtube.com/@${id}`,
    icon: (
        <svg viewBox="0 0 24 24" fill="currentColor" width="15" height="15">
          <path d="M23.498 6.186a3.016 3.016 0 0 0-2.122-2.136C19.505 3.545 12 3.545 12 3.545s-7.505 0-9.377.505A3.017 3.017 0 0 0 .502 6.186C0 8.07 0 12 0 12s0 3.93.502 5.814a3.016 3.016 0 0 0 2.122 2.136c1.871.505 9.376.505 9.376.505s7.505 0 9.377-.505a3.015 3.015 0 0 0 2.122-2.136C24 15.93 24 12 24 12s0-3.93-.502-5.814zM9.545 15.568V8.432L15.818 12l-6.273 3.568z"/>
        </svg>
    ),
  },
  {
    key: 'spotifyId',
    label: 'Spotify',
    href: (id) => `https://open.spotify.com/artist/${id}`,
    icon: (
        <svg viewBox="0 0 24 24" fill="currentColor" width="15" height="15">
          <path d="M12 0C5.4 0 0 5.4 0 12s5.4 12 12 12 12-5.4 12-12S18.66 0 12 0zm5.521 17.34c-.24.359-.66.48-1.021.24-2.82-1.74-6.36-2.101-10.561-1.141-.418.122-.779-.179-.899-.539-.12-.421.18-.78.54-.9 4.56-1.021 8.52-.6 11.64 1.32.42.18.479.659.301 1.02zm1.44-3.3c-.301.42-.841.6-1.262.3-3.239-1.98-8.159-2.58-11.939-1.38-.479.12-1.02-.12-1.14-.6-.12-.48.12-1.021.6-1.141C9.6 9.9 15 10.561 18.72 12.84c.361.181.54.78.241 1.2zm.12-3.36C15.24 8.4 8.82 8.16 5.16 9.301c-.6.179-1.2-.181-1.38-.721-.18-.601.18-1.2.72-1.381 4.26-1.26 11.28-1.02 15.721 1.621.539.3.719 1.02.419 1.56-.299.421-1.02.599-1.559.3z"/>
        </svg>
    ),
  },
  {
    key: 'instagramId',
    label: 'Instagram',
    href: (id) => `https://instagram.com/${id}`,
    icon: (
        <svg viewBox="0 0 24 24" fill="currentColor" width="15" height="15">
          <path d="M12 2.163c3.204 0 3.584.012 4.85.07 3.252.148 4.771 1.691 4.919 4.919.058 1.265.069 1.645.069 4.849 0 3.205-.012 3.584-.069 4.849-.149 3.225-1.664 4.771-4.919 4.919-1.266.058-1.644.07-4.85.07-3.204 0-3.584-.012-4.849-.07-3.26-.149-4.771-1.699-4.919-4.92-.058-1.265-.07-1.644-.07-4.849 0-3.204.013-3.583.07-4.849.149-3.227 1.664-4.771 4.919-4.919 1.266-.057 1.645-.069 4.849-.069zM12 0C8.741 0 8.333.014 7.053.072 2.695.272.273 2.69.073 7.052.014 8.333 0 8.741 0 12c0 3.259.014 3.668.072 4.948.2 4.358 2.618 6.78 6.98 6.98C8.333 23.986 8.741 24 12 24c3.259 0 3.668-.014 4.948-.072 4.354-.2 6.782-2.618 6.979-6.98.059-1.28.073-1.689.073-4.948 0-3.259-.014-3.667-.072-4.947-.196-4.354-2.617-6.78-6.979-6.98C15.668.014 15.259 0 12 0zm0 5.838a6.162 6.162 0 1 0 0 12.324 6.162 6.162 0 0 0 0-12.324zM12 16a4 4 0 1 1 0-8 4 4 0 0 1 0 8zm6.406-11.845a1.44 1.44 0 1 0 0 2.881 1.44 1.44 0 0 0 0-2.881z"/>
        </svg>
    ),
  },
  {
    key: 'tiktokId',
    label: 'TikTok',
    href: (id) => `https://tiktok.com/@${id}`,
    icon: (
        <svg viewBox="0 0 24 24" fill="currentColor" width="15" height="15">
          <path d="M19.59 6.69a4.83 4.83 0 0 1-3.77-4.25V2h-3.45v13.67a2.89 2.89 0 0 1-2.88 2.5 2.89 2.89 0 0 1-2.89-2.89 2.89 2.89 0 0 1 2.89-2.89c.28 0 .54.04.79.1V9.01a6.33 6.33 0 0 0-.79-.05 6.34 6.34 0 0 0-6.34 6.34 6.34 6.34 0 0 0 6.34 6.34 6.34 6.34 0 0 0 6.33-6.34V8.69a8.18 8.18 0 0 0 4.78 1.52V6.75a4.85 4.85 0 0 1-1.01-.06z"/>
        </svg>
    ),
  },
  {
    key: 'personalWebUrl',
    label: 'Website',
    href: (url) => url,
    isUrl: true,
    icon: (
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" width="15" height="15">
          <circle cx="12" cy="12" r="10"/>
          <line x1="2" y1="12" x2="22" y2="12"/>
          <path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"/>
        </svg>
    ),
  },
];

function getInitials(username) {
  if (!username) return '?';
  return username.slice(0, 2).toUpperCase();
}

function User() {
  const { username } = useParams();
  const [userData, setUserData] = useState(null);
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!username) return;
    const load = async () => {
      setLoading(true);
      setError(null);
      try {
        const [userRes, projRes] = await Promise.allSettled([
          userAPI.getUserByUsername(username),
          projectsAPI.getPublishedByUser(username),
        ]);

        if (userRes.status === 'fulfilled') {
          setUserData(userRes.value.data ?? userRes.value);
        } else {
          setError('User not found.');
        }

        if (projRes.status === 'fulfilled') {
          const projectsData = projRes.value.data ?? [];

          // Pobieranie okładek
          const projectsWithCovers = await Promise.all(
              projectsData.map(async (project) => {
                try {
                  const coverRes = await projectsAPI.getProjectCover(project.id);
                  return {
                    ...project,
                    coverUrl: coverRes.data?.url || null
                  };
                } catch {
                  return { ...project, coverUrl: null };
                }
              })
          );

          setProjects(projectsWithCovers);
        }
      } catch {
        setError('Could not load profile.');
      } finally {
        setLoading(false);
      }
    };
    load();
  }, [username]);

  if (loading) return (
      <div className="flex items-center gap-3 px-6 py-3 bg-white/30 backdrop-blur-sm rounded-xl border border-stone-300/50 text-slate-800 font-medium w-fit mt-8 mx-auto">
        <div className="h-2 w-2 rounded-full bg-amber-400 animate-pulse" />
        Loading profile...
      </div>
  );

  if (error) return (
      <div className="px-6 py-4 bg-red-900/30 border border-red-700 rounded-xl text-white text-sm mt-8 mx-auto w-fit">
        {error}
      </div>
  );

  if (!userData) return null;

  const social = userData.social || {};
  const activeSocials = SOCIAL_LINKS.filter((s) => social[s.key]);

  return (
      <div className="flex flex-col sm:flex-row gap-6 p-6 sm:p-8 items-start justify-center">

        {/* Left — profile card */}
        <div className="w-full sm:w-72 flex-shrink-0">
          <div className="shadow-xl rounded-4xl overflow-hidden border border-stone-300/50">
            <div className="bg-linear-to-r from-slate-800 via-slate-700 to-slate-800 px-6 py-4 flex justify-between items-center">
              <h2 className="text-xl font-extrabold tracking-tight text-amber-50">Artist</h2>
              <div className="h-2 w-2 rounded-full bg-amber-400 animate-pulse" />
            </div>

            <div className="bg-white/20 border border-[#CCC5B9]/90 px-6 py-6 flex flex-col gap-5">

              {/* Avatar + identity */}
              <div className="flex flex-col items-center gap-3 pb-4 border-b border-stone-400/30">
                <div className="w-20 h-20 rounded-full bg-slate-700 border-2 border-amber-400/60 flex items-center justify-center text-amber-50 text-2xl font-extrabold select-none">
                  {getInitials(userData.username || username)}
                </div>
                <div className="text-center">
                  <p className="text-lg font-extrabold tracking-tight text-slate-800">
                    {userData.username || username}
                  </p>
                  {userData.email && (
                      <p className="text-xs text-stone-500 mt-0.5">{userData.email}</p>
                  )}
                </div>

                {activeSocials.length > 0 && (
                    <div className="flex items-center gap-2 mt-1 flex-wrap justify-center">
                      {activeSocials.map((s) => (
                          <a
                              key={s.key}
                              href={s.href(social[s.key])}
                              target="_blank"
                              rel="noopener noreferrer"
                              title={s.label}
                              className="w-7 h-7 flex items-center justify-center rounded-full bg-white/40 hover:bg-white/70 border border-stone-300/50 text-stone-600 hover:text-slate-800 transition-all duration-150"
                          >
                            {s.icon}
                          </a>
                      ))}
                    </div>
                )}
              </div>
              {(userData.joinedAt || userData.scoreCount !== undefined || projects.length > 0) && (
                  <div className="flex flex-col gap-2">
                    <p className="text-xs font-semibold tracking-widest text-stone-500 uppercase">Information</p>
                    {userData.joinedAt && (
                        <div className="flex justify-between items-center py-2 px-3 bg-white/30 rounded-xl border border-stone-300/40 text-sm">
                          <span className="text-stone-500 font-medium">Joined</span>
                          <span className="text-slate-800 font-semibold">
                      {new Date(userData.joinedAt).toLocaleDateString('pl-PL', { year: 'numeric', month: 'long' })}
                    </span>
                        </div>
                    )}
                    <div className="flex justify-between items-center py-2 px-3 bg-white/30 rounded-xl border border-stone-300/40 text-sm">
                      <span className="text-stone-500 font-medium">Music Scores</span>
                      <span className="text-slate-800 font-semibold">{projects.length}</span>
                    </div>
                  </div>
              )}
              {activeSocials.length > 0 && (
                  <div className="flex flex-col gap-2">
                    <p className="text-xs font-semibold tracking-widest text-stone-500 uppercase">Social</p>
                    {activeSocials.map((s) => (
                        <a
                            key={s.key}
                            href={s.href(social[s.key])}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="flex items-center gap-3 py-2 px-3 bg-white/30 hover:bg-white/50 rounded-xl border border-stone-300/40 hover:border-stone-400/60 text-sm font-medium text-slate-700 hover:text-slate-900 transition-all duration-150 no-underline"
                        >
                          <span className="text-stone-500">{s.icon}</span>
                          <span>{s.label}</span>
                          <span className="ml-auto text-xs text-stone-400 font-normal truncate max-w-28">
                      {s.isUrl ? social[s.key] : `@${social[s.key]}`}
                    </span>
                        </a>
                    ))}
                  </div>
              )}
            </div>
          </div>
        </div>

        {/* Right — projects with covers */}
        <div className="w-full flex-1 min-w-0">
          <div className="shadow-xl rounded-4xl overflow-hidden border border-stone-300/50">
            <div className="bg-linear-to-r from-slate-800 via-slate-700 to-slate-800 px-6 py-4 flex justify-between items-center">
              <h2 className="text-xl font-extrabold tracking-tight text-amber-50">Music Scores</h2>
              <span className="text-xs text-stone-400 font-medium">{projects.length} score{projects.length !== 1 ? 's' : ''}</span>
            </div>

            <div className="bg-white/20 border border-[#CCC5B9]/90 px-6 py-6">
              {projects.length === 0 ? (
                  <div className="flex flex-col items-center justify-center py-12 gap-3">
                    <span className="text-5xl opacity-20 select-none">𝄽</span>
                    <p className="text-stone-500 text-sm italic">No published music scores</p>
                  </div>
              ) : (
                  <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
                    {projects.map((p) => (
                        <div
                            key={p.id}
                            className="group bg-white/30 rounded-xl border border-stone-300/40 hover:bg-white/40 hover:border-stone-400/60 transition-all duration-200 overflow-hidden"
                        >
                          {/* Cover */}
                          <div className="relative w-full aspect-square bg-gradient-to-br from-slate-700 to-slate-900 overflow-hidden">
                            {p.coverUrl ? (
                                <img
                                    src={p.coverUrl}
                                    alt={p.title || 'Project cover'}
                                    className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                                    onError={(e) => {
                                      e.target.style.display = 'none';
                                      const placeholder = e.target.parentElement.querySelector('.placeholder-icon');
                                      if (placeholder) placeholder.style.display = 'flex';
                                    }}
                                />
                            ) : null}
                            <div
                                className="placeholder-icon absolute inset-0 flex items-center justify-center text-5xl text-white/20 select-none"
                                style={{ display: p.coverUrl ? 'none' : 'flex' }}
                            >
                              𝄞
                            </div>
                            {p.public && (
                                <span className="absolute top-2 right-2 bg-green-500/90 text-white text-[10px] font-bold px-2 py-0.5 rounded-full uppercase tracking-wider">
                          Published
                        </span>
                            )}
                          </div>

                          {/* Info */}
                          <div className="p-3">
                            <p className="text-sm font-semibold text-slate-800 truncate">
                              {p.title || 'Untitled'}
                            </p>
                            {p.creator && (
                                <p className="text-xs text-stone-500 truncate">{p.creator}</p>
                            )}
                          </div>
                        </div>
                    ))}
                  </div>
              )}
            </div>
          </div>
        </div>
      </div>
  );
}

export default User;
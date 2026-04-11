import { useState } from 'react';
import { Link } from 'react-router-dom';

const scoresData = [
    { id: 1, title: "Nocturne Op. 9 No. 2", composer: "Frédéric Chopin", difficulty: "Advanced", key: "E♭ Major", downloads: "12.4k", imageUrl: "https://via.placeholder.com/300x200?text=Chopin" },
    { id: 2, title: "Moonlight Sonata", composer: "Ludwig van Beethoven", difficulty: "Intermediate", key: "C♯ Minor", downloads: "25.1k", imageUrl: "https://via.placeholder.com/300x200?text=Beethoven" },
    { id: 3, title: "Clair de Lune", composer: "Claude Debussy", difficulty: "Intermediate", key: "D♭ Major", downloads: "18.7k", imageUrl: "https://via.placeholder.com/300x200?text=Debussy" },
    { id: 4, title: "Für Elise", composer: "Ludwig van Beethoven", difficulty: "Intermediate", key: "A Minor", downloads: "32.3k", imageUrl: "https://via.placeholder.com/300x200?text=Beethoven" },
    { id: 5, title: "The Entertainer", composer: "Scott Joplin", difficulty: "Advanced", key: "C Major", downloads: "8.9k", imageUrl: "https://via.placeholder.com/300x200?text=Joplin" },
    { id: 6, title: "Canon in D", composer: "Johann Pachelbel", difficulty: "Beginner", key: "D Major", downloads: "41.2k", imageUrl: "https://via.placeholder.com/300x200?text=Pachelbel" },
    { id: 7, title: "Gymnopédie No. 1", composer: "Erik Satie", difficulty: "Beginner", key: "D Major", downloads: "6.5k", imageUrl: "https://via.placeholder.com/300x200?text=Satie" },
    { id: 8, title: "Waltz in A Minor", composer: "Frédéric Chopin", difficulty: "Advanced", key: "A Minor", downloads: "9.3k", imageUrl: "https://via.placeholder.com/300x200?text=Chopin" },
];

function Scores() {
    const [searchTerm, setSearchTerm] = useState('');
    const [difficultyFilter, setDifficultyFilter] = useState('All');
    const [sortBy, setSortBy] = useState('popular');

    const filteredScores = scoresData.filter(score => {
        const matchesSearch = score.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
                              score.composer.toLowerCase().includes(searchTerm.toLowerCase());
        const matchesDifficulty = difficultyFilter === 'All' || score.difficulty === difficultyFilter;
        return matchesSearch && matchesDifficulty;
    }).sort((a, b) => {
        if (sortBy === 'popular') return parseInt(b.downloads) - parseInt(a.downloads);
        if (sortBy === 'title') return a.title.localeCompare(b.title);
        if (sortBy === 'composer') return a.composer.localeCompare(b.composer);
        return 0;
    });

    return (
        <div className="min-h-auto mt-15 bg-[#E8E4DD] pt-24 pb-12 px-4">
            <div className="max-w-7xl mx-auto">
                <div className="mb-8">
                    <h1 className="text-3xl font-bold text-[#050505] mb-2">Sheet Music Library</h1>
                    <p className="text-[#2c2c2c]">Discover and download free sheet music from classical to contemporary</p>
                </div>

                <div className="bg-[#f7f4ee] rounded-xl border border-[#CCC5B9] p-4 mb-8">
                    <div className="flex flex-col md:flex-row gap-4">
                        <div className="flex-1 relative">
                            <svg className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-[#2c2c2c]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                            </svg>
                            <input
                                type="text"
                                placeholder="Search by title or composer..."
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                                className="w-full pl-10 pr-4 py-2 bg-[#E8E4DD] border border-[#CCC5B9] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#365603] text-[#050505]"
                            />
                        </div>
                        <select
                            value={difficultyFilter}
                            onChange={(e) => setDifficultyFilter(e.target.value)}
                            className="px-4 py-2 bg-[#E8E4DD] border border-[#CCC5B9] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#365603] text-[#050505]"
                        >
                            <option value="All">All Difficulties</option>
                            <option value="Beginner">Beginner</option>
                            <option value="Intermediate">Intermediate</option>
                            <option value="Advanced">Advanced</option>
                        </select>
                        <select
                            value={sortBy}
                            onChange={(e) => setSortBy(e.target.value)}
                            className="px-4 py-2 bg-[#E8E4DD] border border-[#CCC5B9] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#365603] text-[#050505]"
                        >
                            <option value="popular">Most Popular</option>
                            <option value="title">Title A-Z</option>
                            <option value="composer">Composer A-Z</option>
                        </select>
                    </div>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                    {filteredScores.map(score => (
                        <div key={score.id} className="bg-[#f7f4ee] border border-[#CCC5B9] rounded-xl overflow-hidden hover:shadow-xl transition-all hover:-translate-y-1 cursor-pointer group">
                            <div className="aspect-[4/3] bg-gradient-to-br from-[#365603]/20 to-[#213106]/20 flex items-center justify-center">
                                <span className="text-6xl opacity-50 group-hover:opacity-100 transition-opacity">🎼</span>
                            </div>
                            <div className="p-4">
                                <h3 className="font-bold text-[#050505] truncate">{score.title}</h3>
                                <p className="text-sm text-[#2c2c2c] mt-1">{score.composer}</p>
                                <div className="flex flex-wrap gap-2 mt-3">
                                    <span className="text-xs px-2 py-1 bg-[#E8E4DD] rounded-full text-[#050505]">{score.difficulty}</span>
                                    <span className="text-xs px-2 py-1 bg-[#E8E4DD] rounded-full text-[#050505]">{score.key}</span>
                                </div>
                                <div className="flex justify-between items-center mt-4">
                                    <span className="text-xs text-[#2c2c2c]">⬇️ {score.downloads}</span>
                                    <button className="px-4 py-1 bg-[#365603] text-[#f7f4ee] rounded-full text-sm hover:bg-[#213106] transition-all">
                                        Download
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>

                {filteredScores.length === 0 && (
                    <div className="text-center py-12">
                        <span className="text-6xl mb-4 block">🎵</span>
                        <p className="text-[#2c2c2c]">No scores found. Try adjusting your search.</p>
                    </div>
                )}
            </div>
        </div>
    );
}

export default Scores;
import { Link } from 'react-router-dom';

function Download() {
    return (
        <div className="min-h-auto mt-15 bg-[#E8E4DD] pt-24 pb-12 px-4">
            <div className="max-w-6xl mx-auto">
                <div className="text-center mb-12">
                    <h1 className="text-4xl font-bold text-[#050505] mb-4">Download MUSE App</h1>
                    <p className="text-lg text-[#2c2c2c] max-w-2xl mx-auto">
                        Access thousands of sheet music scores offline, create playlists, and practice with our interactive tools.
                    </p>
                </div>

                <div className="bg-[#f7f4ee] rounded-2xl border border-[#CCC5B9] p-8 mb-8">
                    <div className="grid md:grid-cols-2 gap-8">
                        <div className="text-center p-6 bg-[#E8E4DD] rounded-xl">
                            <span className="text-5xl mb-4 block">Icon...</span>
                            <h3 className="text-xl font-bold text-[#050505] mb-2">Windows</h3>
                            <p className="text-[#2c2c2c] text-sm mb-4">Windows 10/11 (64-bit)</p>
                            <button className="w-full px-6 py-3 bg-[#365603] text-[#f7f4ee] rounded-full hover:bg-[#213106] transition-all font-semibold">
                                Download for Windows
                            </button>
                        </div>
                        <div className="text-center p-6 bg-[#E8E4DD] rounded-xl">
                            <span className="text-5xl mb-4 block">Icon...</span>
                            <h3 className="text-xl font-bold text-[#050505] mb-2">macOS</h3>
                            <p className="text-[#2c2c2c] text-sm mb-4">macOS 11.0 or later</p>
                            <button className="w-full px-6 py-3 bg-[#365603] text-[#f7f4ee] rounded-full hover:bg-[#213106] transition-all font-semibold">
                                Download for macOS
                            </button>
                        </div>
                    </div>
                </div>

                <div className="bg-[#f7f4ee] rounded-2xl border border-[#CCC5B9] p-8">
                    <h2 className="text-2xl font-bold text-[#050505] mb-6 text-center">System Requirements</h2>
                    <div className="grid md:grid-cols-3 gap-6">
                        <div className="text-center">
                            <div className="w-12 h-12 bg-[#365603]/10 rounded-full flex items-center justify-center mx-auto mb-3">
                                <span className="text-xl">💾</span>
                            </div>
                            <h3 className="font-semibold text-[#050505] mb-1">Storage</h3>
                            <p className="text-sm text-[#2c2c2c]">500 MB available space</p>
                        </div>
                        <div className="text-center">
                            <div className="w-12 h-12 bg-[#365603]/10 rounded-full flex items-center justify-center mx-auto mb-3">
                                <span className="text-xl">📝</span>
                            </div>
                            <h3 className="font-semibold text-[#050505] mb-1">RAM</h3>
                            <p className="text-sm text-[#2c2c2c]">4 GB minimum, 8 GB recommended</p>
                        </div>
                        <div className="text-center">
                            <div className="w-12 h-12 bg-[#365603]/10 rounded-full flex items-center justify-center mx-auto mb-3">
                                <span className="text-xl">🌐</span>
                            </div>
                            <h3 className="font-semibold text-[#050505] mb-1">Internet</h3>
                            <p className="text-sm text-[#2c2c2c]">Required for initial download and updates</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Download;
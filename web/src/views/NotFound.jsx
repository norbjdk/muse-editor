import { Link } from 'react-router-dom';

function NotFound() {
    return (
        <div className="min-h-auto mt-15 py-20 bg-[#E8E4DD] flex items-center justify-center px-4">
            <div className="text-center max-w-lg">
                <div className="w-32 h-32 mx-auto mb-8 bg-[#365603]/10 rounded-full flex items-center justify-center">
                    <span className="text-6xl">🎵</span>
                </div>
                <h1 className="text-9xl font-black text-[#365603] mb-4">404</h1>
                <h2 className="text-2xl font-bold text-[#050505] mb-4">Page Not Found</h2>
                <p className="text-[#2c2c2c] mb-8">
                    Oops! The sheet music you're looking for seems to have hit a wrong note.
                    Let's get you back to the right melody.
                </p>
                <Link 
                    to="/" 
                    className="inline-flex items-center gap-2 px-6 py-3 bg-[#365603] text-[#f7f4ee] rounded-full hover:bg-[#213106] transition-all font-semibold"
                >
                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 19l-7-7m0 0l7-7m-7 7h18" />
                    </svg>
                    Back to Home
                </Link>
            </div>
        </div>
    );
}

export default NotFound;
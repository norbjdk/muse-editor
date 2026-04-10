import { Link } from "react-router-dom";
import SearchBar from "./SearchBar";
import Logo from "../assets/images/logo.png";

function Navbar() {
    return (
        <nav className="bg-[#E8E4DD] backdrop-blur-md border-b border-[#CCC5B9]/10 shadow-lg p-4 font-sans w-full fixed top-0 z-50 transition-all">
            {/* Desktop */}
            <div className="mx-auto hidden md:grid grid-cols-3 items-center w-[80%]">
                <div className="flex items-center gap-6">
                    <Link 
                        to="/scores" 
                        className="text-[#050505] text-sm font-medium hover:text-[#4d4643] transition-colors relative group">
                        Scores
                        <span className="absolute -bottom-1 left-0 w-0 h-0.5 bg-[#2c2c2c] transition-all group-hover:w-full"></span>
                    </Link>
                    <div className="w-full max-w-100">
                        <SearchBar />
                    </div>
                </div>
                <div className="flex justify-center">
                    <Link 
                        to="/" 
                        className="text-2xl font-bold tracking-tighter text-[#050505] hover:scale-105 transition-transform flex items-center gap-1">
                        <span className="w-8 h-8 border-2 rounded-2xl flex items-end justify-center text-[#050505] text-lg font-black">
                            <img src= { Logo } alt="logo" />
                        </span>
                        MUSE
                    </Link>
                </div>
                <div className="flex justify-end items-center gap-8">
                    <Link 
                        to="/download" 
                        className="text-[#050505] text-sm hover:text-[#2c2c2c] transition-colors">
                        Download
                    </Link>
                    <Link 
                        to="/support" 
                        className="text-[#050505] text-sm hover:text-[#2c2c2c] transition-colors">
                        Support
                    </Link>
                    <Link 
                        to="/account" 
                        className="px-5 py-2 bg-[#F4F4F4] text-[#252422] rounded-lg text-sm font-bold hover:bg-[#96918f] hover:text-[#FFFCF2] transition-all">
                        Account
                    </Link>
                </div>
            </div>

            {/* Mobile */}
            <div className="md:hidden flex justify-between items-center">
                 <Link 
                        to="/" 
                        className="text-2xl font-bold tracking-tighter text-[#FFFCF2] hover:scale-105 transition-transform flex items-center gap-2">
                        <span className="w-8 h-8 border-2 rounded-2xl flex items-end justify-center text-[#252422] text-lg font-black">
                            <img src= { Logo } alt="logo" />
                        </span>
                        MUSE
                    </Link>
                 <button className="text-[#FFFCF2]">
                    <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16m-7 6h7"></path></svg>
                 </button>
            </div>
        </nav>
    );
}

export default Navbar;
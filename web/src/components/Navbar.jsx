import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import SearchBar from "./SearchBar";
import Logo from "../assets/images/logo.png";

function Navbar() {
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

    const handleLogout = () => {
        logout();
        setIsDropdownOpen(false);
        navigate("/");
    };

    const closeDropdown = () => {
        setIsDropdownOpen(false);
    };

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (!event.target.closest('.dropdown-container')) {
                setIsDropdownOpen(false);
            }
        };

        document.addEventListener('click', handleClickOutside);
        return () => document.removeEventListener('click', handleClickOutside);
    }, []);

    return (
        <nav className="bg-[#E8E4DD] backdrop-blur-md border-b border-[#CCC5B9]/10 shadow-lg p-4 font-sans w-full fixed top-0 z-50 transition-all">
            <div className="mx-auto hidden md:grid grid-cols-3 items-center w-[80%]">
                <div className="flex items-center gap-6">
                    <Link 
                        to="/scores" 
                        className="text-[#050505] text-sm font-medium hover:text-[#365603] transition-colors relative group">
                        Scores
                        <span className="absolute -bottom-1 left-0 w-0 h-0.5 bg-[#365603] transition-all group-hover:w-full"></span>
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
                            <img src={Logo} alt="logo" />
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

                    {user ? (
                        <div className="relative dropdown-container">
                            <button 
                                onClick={() => setIsDropdownOpen(!isDropdownOpen)}
                                className="flex items-center gap-2 text-[#050505] hover:text-[#365603] focus:outline-none transition-colors"
                            >
                                <div className="flex items-center gap-2">
                                    <div className="w-8 h-8 bg-[#365603] rounded-full flex items-center justify-center text-white font-bold text-sm">
                                        {user.username.charAt(0).toUpperCase()}
                                    </div>
                                    <span className="font-medium text-sm">{user.username}</span>
                                    <svg 
                                        className={`w-4 h-4 transition-transform ${isDropdownOpen ? 'rotate-180' : ''}`}
                                        fill="none" 
                                        stroke="currentColor" 
                                        viewBox="0 0 24 24"
                                    >
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                                    </svg>
                                </div>
                            </button>
                            
                            {isDropdownOpen && (
                                <div className="absolute right-0 mt-2 w-64 bg-[#f7f4ee] border border-[#CCC5B9] rounded-xl shadow-xl py-2 z-50">
                                    <div className="px-4 py-3 border-b border-[#CCC5B9]">
                                        <p className="text-[#050505] font-semibold">{user.username}</p>
                                        <p className="text-[#2c2c2c] text-sm">{user.email}</p>
                                        <p className="text-[#365603] text-xs mt-1 font-medium">
                                            {user.role === 'Artist' ? '🎤 Artist' : '👂 Listener'}
                                        </p>
                                    </div>

                                    <Link 
                                        to="/profile" 
                                        onClick={closeDropdown}
                                        className="flex items-center px-4 py-3 text-[#050505] hover:bg-[#E8E4DD] transition-colors"
                                    >
                                        <svg className="w-5 h-5 mr-3 text-[#365603]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                                        </svg>
                                        Profile
                                    </Link>
                                    
                                    {user.role === 'Artist' && (
                                        <Link 
                                            to="/upload" 
                                            onClick={closeDropdown}
                                            className="flex items-center px-4 py-3 text-[#050505] hover:bg-[#E8E4DD] transition-colors"
                                        >
                                            <svg className="w-5 h-5 mr-3 text-[#365603]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a3 3 0 01-3-3V5a3 3 0 013-3h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a3 3 0 01-3 3z" />
                                            </svg>
                                            Upload Music
                                        </Link>
                                    )}
                                    
                                    <Link 
                                        to="/settings" 
                                        onClick={closeDropdown}
                                        className="flex items-center px-4 py-3 text-[#050505] hover:bg-[#E8E4DD] transition-colors"
                                    >
                                        <svg className="w-5 h-5 mr-3 text-[#365603]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                                        </svg>
                                        Settings
                                    </Link>
                                    
                                    <div className="border-t border-[#CCC5B9] my-2"></div>
                                    
                                    <button
                                        onClick={handleLogout}
                                        className="flex items-center w-full text-left px-4 py-3 text-red-600 hover:bg-[#E8E4DD] transition-colors"
                                    >
                                        <svg className="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                                        </svg>
                                        Logout
                                    </button>
                                </div>
                            )}
                        </div>
                    ) : (
                        <div className="relative dropdown-container">
                            <button
                                onClick={() => setIsDropdownOpen(!isDropdownOpen)}
                                className="px-5 py-2 bg-[#365603] border text-[#f7f4ee] rounded-full text-sm font-bold hover:bg-[#213106] hover:text-[#FFFCF2] transition-all"
                            >
                                Account
                            </button>
                            
                            {isDropdownOpen && (
                                <div className="absolute right-0 mt-2 w-48 bg-[#f7f4ee] border border-[#CCC5B9] rounded-xl shadow-xl py-2 z-50">
                                    <Link 
                                        to="/login" 
                                        onClick={closeDropdown}
                                        className="flex items-center px-4 py-3 text-[#050505] hover:bg-[#E8E4DD] transition-colors"
                                    >
                                        <svg className="w-5 h-5 mr-3 text-[#365603]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 16l-4-4m0 0l4-4m-4 4h14m-5 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h7a3 3 0 013 3v1" />
                                        </svg>
                                        Login
                                    </Link>
                                    <Link 
                                        to="/register" 
                                        onClick={closeDropdown}
                                        className="flex items-center px-4 py-3 text-[#050505] hover:bg-[#E8E4DD] transition-colors"
                                    >
                                        <svg className="w-5 h-5 mr-3 text-[#365603]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z" />
                                        </svg>
                                        Register
                                    </Link>
                                </div>
                            )}
                        </div>
                    )}
                </div>
            </div>

            <div className="md:hidden flex justify-between items-center">
                <Link 
                    to="/" 
                    className="text-2xl font-bold tracking-tighter text-[#050505] hover:scale-105 transition-transform flex items-center gap-2">
                    <span className="w-8 h-8 border-2 rounded-2xl flex items-end justify-center text-[#252422] text-lg font-black">
                        <img src={Logo} alt="logo" />
                    </span>
                    MUSE
                </Link>
                <button 
                    onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
                    className="text-[#050505] p-2"
                >
                    <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16m-7 6h7"></path>
                    </svg>
                </button>
            </div>

            {isMobileMenuOpen && (
                <div className="md:hidden mt-4 pt-4 border-t border-[#CCC5B9]">
                </div>
            )}
        </nav>
    );
}

export default Navbar;
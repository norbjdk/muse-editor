import Background from "../assets/bg.png";

import { Link } from "react-router-dom";

import { Home } from "lucide-react";
import { User } from "lucide-react";
import { BookOpen } from "lucide-react";
import { FileMusic } from "lucide-react";
import { Library } from "lucide-react";
import { LogOut } from "lucide-react";

import { useAuth } from "../contexts/AuthContext";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

function Hero() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

   const handleLogout = () => {
        logout();
        setIsDropdownOpen(false);
        navigate("/");
    };

  return (
    <div 
      className="bg-cover bg-center flex flex-col items-center justify-center gap-10 w-full px-6 py-12 min-h-48 text-center"
      style={{ backgroundImage: `url(${Background})` }} 
    >
      <div className="max-w-2xl flex flex-col gap-4">        
        <h1 className="text-6xl md:text-7xl font-extrabold tracking-tight text-[#E8E4DD] drop-shadow-sm text-shadow-slate-700 text-shadow-md">
          MUSE
        </h1>

        <p className="text-base md:text-2xl text-white/80 font-thin drop-shadow-sm drop-shadow-slate-500">
          Simple music notation software, that allows single artists or bands to create their own music sheets for desired instruments.
        </p>
      </div>
      <div className="flex flex-col sm:flex-row items-center gap-4 w-full justify-center">
        <Link to="/" className={`flex items-center justify-between gap-4 w-full sm:w-auto px-3 py-2 bg-white/50 hover:bg-white/70 text-slate-800 font-medium rounded-xl border border-stone-900/10 shadow-sm hover:shadow-md active:scale-[0.98] transition-all duration-200 cursor-pointer backdrop-blur-sm`}>
            Home
            <Home size={32} className={`text-slate-500`}/>
        </Link>
        {/* <Link className={`flex items-center justify-between gap-4 w-full sm:w-auto px-3 py-2 bg-white/50 hover:bg-white/70 text-slate-800 font-medium rounded-xl border border-stone-900/10 shadow-sm hover:shadow-md active:scale-[0.98] transition-all duration-200 cursor-pointer backdrop-blur-sm`}>
            Library
            <Library size={32} className={`text-slate-500`}/>
        </Link> */}
        <Link className={`flex items-center justify-between gap-4 w-full sm:w-auto px-3 py-2 bg-white/50 hover:bg-white/70 text-slate-800 font-medium rounded-xl border border-stone-900/10 shadow-sm hover:shadow-md active:scale-[0.98] transition-all duration-200 cursor-pointer backdrop-blur-sm`}>
            Editor
            <FileMusic size={32} className={`text-slate-500`}/>
        </Link>
        <Link className={`flex items-center justify-between gap-4 w-full sm:w-auto px-3 py-2 bg-white/50 hover:bg-white/70 text-slate-800 font-medium rounded-xl border border-stone-900/10 shadow-sm hover:shadow-md active:scale-[0.98] transition-all duration-200 cursor-pointer backdrop-blur-sm`}>
            Docs
            <BookOpen size={32} className={`text-slate-500`}/>
        </Link>
        <Link to="/account" className={`flex items-center justify-between gap-4 w-full sm:w-auto px-3 py-2 bg-white/50 hover:bg-white/70 text-slate-800 font-medium rounded-xl border border-stone-900/10 shadow-sm hover:shadow-md active:scale-[0.98] transition-all duration-200 cursor-pointer backdrop-blur-sm`}>
            {user ? user.username : "Account"}
            <User size={32} className={`text-slate-500`}/>
        </Link>
        {user && (
          <button
          onClick={handleLogout}
            className={`flex items-center justify-between gap-4 w-full sm:w-auto px-3 py-2 bg-white/50 hover:bg-white/70 text-slate-800 font-medium rounded-xl border border-stone-900/10 shadow-sm hover:shadow-md active:scale-[0.98] transition-all duration-200 cursor-pointer backdrop-blur-sm`}
          >
            Logout
            <LogOut size={32} className={`text-slate-500`} />
          </button>
        )}
      </div>
    </div>
  );
}

export default Hero;

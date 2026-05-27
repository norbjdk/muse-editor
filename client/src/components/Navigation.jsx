import { Link } from "react-router-dom";

import { Home } from "lucide-react";
import { User } from "lucide-react";
import { BookOpen } from "lucide-react";
import { FileMusic } from "lucide-react";
import { Library } from "lucide-react";

function Navigation() {
    return(
        <div className={`flex flex-col gap-5 p-2`}>
            <Link className={`flex items-center justify-between w-full sm:w-auto px-3 py-2 bg-white/50 hover:bg-white/70 text-slate-800 font-medium rounded-xl border border-stone-900/10 shadow-sm hover:shadow-md active:scale-[0.98] transition-all duration-200 cursor-pointer backdrop-blur-sm`}>
                Library
                <Library size={32} className={`text-slate-500`}/>
            </Link>
            <Link className={`flex items-center justify-between w-full sm:w-auto px-3 py-2 bg-white/50 hover:bg-white/70 text-slate-800 font-medium rounded-xl border border-stone-900/10 shadow-sm hover:shadow-md active:scale-[0.98] transition-all duration-200 cursor-pointer backdrop-blur-sm`}>
                Editor
                <FileMusic size={32} className={`text-slate-500`}/>
            </Link>
            <Link className={`flex items-center justify-between gap-4 w-full sm:w-auto px-3 py-2 bg-white/50 hover:bg-white/70 text-slate-800 font-medium rounded-xl border border-stone-900/10 shadow-sm hover:shadow-md active:scale-[0.98] transition-all duration-200 cursor-pointer backdrop-blur-sm`}>
                Account
                <User size={32} className={`text-slate-500`}/>
            </Link>
            <Link className={`flex items-center justify-between w-full sm:w-auto px-3 py-2 bg-white/50 hover:bg-white/70 text-slate-800 font-medium rounded-xl border border-stone-900/10 shadow-sm hover:shadow-md active:scale-[0.98] transition-all duration-200 cursor-pointer backdrop-blur-sm`}>
                README
                <BookOpen size={32} className={`text-slate-500`}/>
            </Link>
        </div>
    );
}

export default Navigation;
import { Link } from "react-router-dom";
import Logo from "../assets/logo.png";
import Background from "../assets/bg.png";
import { Podcast } from "lucide-react";

function Footer() {
    return(
        <footer className="py-4 text-center text-sm text-gray-200 font-sans w-full border-t-4 border-stone-500 bg-slate-800">
            <div className="flex flex-col justify-center gap-4">
                <div className="flex flex-row justify-evenly">
                    <Link to="/tos" className={`tracking-widest font-extrabold text-[#E8E4DD] drop-shadow-sm text-shadow-slate-700 text-shadow-md`}>Terms of Service</Link>
                    <Link to="privacy" className={`tracking-widest font-extrabold text-[#E8E4DD] drop-shadow-sm text-shadow-slate-700 text-shadow-md`}>Privacy Policy</Link>
                </div>
                <div>
                    <p className={`text-xl font-extrabold tracking-tight text-[#E8E4DD] drop-shadow-sm text-shadow-slate-700 text-shadow-md`}>2026 &#169; MUSE</p>
                </div>
            </div>
        </footer>
    );
}

export default Footer;
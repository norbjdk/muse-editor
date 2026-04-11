import { Link } from "react-router-dom";
import Logo from "../assets/images/logo.png";

function Footer() {
    return(
        <div className="bg-[#E8E4DD]  pt-12 pb-6 px-0 font-sans shadow-2xl backdrop-blur-md">
            <div className="min-w-9/12 max-w-full mx-auto">
                <div className="grid grid-cols-1 md:grid-cols-4 gap-12 mb-12 px-6">
                    <div className="col-span-1 md:col-span-1">
                        <Link to="/" className="text-[#050505] font-bold text-xl tracking-tighter flex items-center gap-2 mb-4">
                            <span className="w-6 h-6 border-2 rounded-2xl flex items-center justify-center text-[#252422] text-sm font-black">
                                <img src={ Logo } alt="logo"/>
                            </span>
                            MUSE
                        </Link>
                        <p className="text-[#2c2c2c] text-sm leading-relaxed">
                            The open-source studio for modern composers. Write, share, and play music without barriers.
                        </p>
                    </div>

                    <div>
                        <h4 className="text-[#050505] font-bold mb-4 text-sm uppercase tracking-widest">App</h4>
                        <ul className="space-y-2 text-sm text-[#050505]">
                            <li><Link to="/scores" className="hover:text-[#2c2c2c] transition-colors">Browse Scores</Link></li>
                            <li><Link to="/download" className="hover:text-[#2c2c2c] transition-colors">Desktop App</Link></li>
                        </ul>
                    </div>

                    <div>
                        <h4 className="text-[#050505] font-bold mb-4 text-sm uppercase tracking-widest">Community</h4>
                        <ul className="space-y-2 text-sm text-[#050505]">
                            <li><a href="https://github.com" className="hover:text-[#2c2c2c] transition-colors">GitHub</a></li>
                            <li><Link to="/contribute" className="hover:text-[#2c2c2c] transition-colors">Contribute</Link></li>
                            <li><Link to="/forum" className="hover:text-[#2c2c2c] transition-colors">Discussions</Link></li>
                        </ul>
                    </div>

                    <div>
                        <h4 className="text-[#050505] font-bold mb-4 text-sm uppercase tracking-widest">Support</h4>
                        <ul className="space-y-2 text-sm text-[#050505]">
                            <li><Link to="/help" className="hover:text-[#2c2c2c] transition-colors">Help Center</Link></li>
                            <li><Link to="/privacy" className="hover:text-[#2c2c2c] transition-colors">Privacy Policy</Link></li>
                            <li><Link to="/copyright" className="hover:text-[#2c2c2c] transition-colors">Copyright</Link></li>
                        </ul>
                    </div>
                </div>

                <div className="border-t border-[#365603] pt-2 flex flex-col md:row justify-between items-center gap-4">
                    <p className="text-[#050505] text-xs">
                        &copy; {new Date().getFullYear()} MUSE is music software. Released under MIT License.
                    </p>
                    <div className="flex gap-6">
                        <a href="#" className="text-[#050505] hover:text-[#494843] transition-colors text-xs font-medium italic">GitHub</a>
                        <a href="#" className="text-[#050505] hover:text-[#494843] transition-colors text-xs font-medium italic">Instagram</a>
                        <a href="#" className="text-[#050505] hover:text-[#494843] transition-colors text-xs font-medium italic">Discord</a>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Footer;
import { useState } from "react";
import { useAuth } from "../contexts/AuthContext";
import { useNavigate } from "react-router-dom";

function Account() {
    const [registerUsername, setRegisterUsername] = useState("");
    const [registerPassword, setRegisterPassword] = useState("");
    const [registerEmail, setRegisterEmail] = useState("");

    const [loginUsername, setLoginUsername] = useState("");
    const [loginPassword, setLoginPassword] = useState("");

    const [registerError, setRegisterError] = useState("");
    const [loginError, setLoginError] = useState("");

    const [registerLoading, setRegisterLoading] = useState(false);
    const [loginLoading, setLoginLoading] = useState(false);

    const { login } = useAuth();
    const { register } = useAuth();
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        setRegisterError('');

        if (!registerUsername || !registerEmail || !registerPassword) {
            return setRegisterError('Please fill all fields');
        }

        setRegisterLoading(true);

        try {
            const result = await register(registerUsername, registerEmail, registerPassword);

            if (result.success) {
                navigate('/');
            } else {
                setRegisterError(result.error);
            }
        } catch (err) {
            setRegisterError('Login failed. Please try again.');
        } finally {
            setRegisterLoading(false);
        }
    }

    const handleLogin = async (e) => {
        e.preventDefault();
        setLoginError('');

        if (!loginUsername || !loginPassword) {
            return setLoginError('Please fill all fields');
        }

        setLoginLoading(true);

        try {
            const result = await login(loginUsername, loginPassword);

            if (result.success) {
                navigate('/');
            } else {
                setLoginError(result.error);
            }
        } catch (err) {
            setLoginError('Login failed. Please try again.');
        } finally {
            setLoginLoading(false);
        }
    }

    return (
        <div className={`flex flex-row gap-8 p-8 justify-around container`}>
            <div className={`flex flex-col w-1/3`}>
                <form onSubmit={handleRegister}>
                    <div className="w-full mx-auto shadow-xl rounded-4xl overflow-hidden border border-stone-300/50">
                        <div className="bg-linear-to-r from-slate-800 via-slate-700 to-slate-800 px-6 py-4 flex justify-between items-center">
                            <h2 className="text-3xl font-extrabold tracking-tight text-amber-50 drop-shadow-md">
                                Sign Up
                            </h2>
                            <div className="h-2 w-2 rounded-full bg-amber-400 animate-pulse" />
                        </div>
                        <div className="bg-white/20 border border-[#CCC5B9]/90 py-8 px-6 flex flex-col gap-5">
                            <div className="flex flex-col gap-1.5">
                                {registerError && (
                                    <div className="mb-6 p-3 bg-red-900/30 border border-red-700 rounded-lg">
                                        <p className="text-white text-sm">{registerError}</p>
                                    </div>
                                )}
                                <label className="text-sm font-semibold text-stone-700 tracking-wide">Username</label>
                                <input
                                    type="text"
                                    placeholder="Enter your username"
                                    required
                                    value={registerUsername}
                                    onChange={(e) => setRegisterUsername(e.target.value)}
                                    className="w-full bg-amber-50/60 border border-stone-300 rounded-xl px-4 py-2.5 text-stone-800 placeholder-stone-400 outline-hidden focus:bg-amber-50 focus:border-stone-500 focus:ring-3 focus:ring-stone-500/20 transition-all duration-200"
                                />
                            </div>
                            <div className="flex flex-col gap-1.5">
                                <label className="text-sm font-semibold text-stone-700 tracking-wide">E-Mail</label>
                                <input
                                    type="email"
                                    placeholder="you@example.com"
                                    required
                                    value={registerEmail}
                                    onChange={(e) => setRegisterEmail(e.target.value)}
                                    className="w-full bg-amber-50/60 border border-stone-300 rounded-xl px-4 py-2.5 text-stone-800 placeholder-stone-400 outline-hidden focus:bg-amber-50 focus:border-stone-500 focus:ring-3 focus:ring-stone-500/20 transition-all duration-200"
                                />
                            </div>
                            <div className="flex flex-col gap-1.5">
                                <label className="text-sm font-semibold text-stone-700 tracking-wide">Password</label>
                                <input
                                    type="password"
                                    placeholder="••••••••"
                                    required
                                    value={registerPassword}
                                    onChange={(e) => setRegisterPassword(e.target.value)}
                                    className="w-full bg-amber-50/60 border border-stone-300 rounded-xl px-4 py-2.5 text-stone-800 placeholder-stone-400 outline-hidden focus:bg-amber-50 focus:border-stone-500 focus:ring-3 focus:ring-stone-500/20 transition-all duration-200"
                                />
                            </div>
                            <button
                                type="submit"
                                disabled={registerLoading}
                                className="mt-2 w-full bg-slate-800 hover:bg-stone-900 text-amber-50 font-medium py-3 rounded-xl shadow-md cursor-pointer hover:shadow-lg active:scale-[0.99] transition-all duration-150">
                                {registerLoading ? (
                                    <>
                                        <div className="animate-spin rounded-full h-5 w-5 border-2 border-white border-t-transparent"></div>
                                        Musing you in...
                                    </>
                                ) : (
                                    'Create account'
                                )}
                            </button>
                        </div>
                    </div>
                </form>
            </div>
            <div className={`flex flex-col w-1/3`}>
                <form onSubmit={handleLogin}>
                    <div className="w-full mx-auto shadow-xl rounded-4xl overflow-hidden border border-stone-300/50">
                        <div className="bg-linear-to-r from-slate-800 via-slate-700 to-slate-800 px-6 py-4 flex justify-between items-center">
                            <h2 className="text-3xl font-extrabold tracking-tight text-amber-50 drop-shadow-md">
                                Sign In
                            </h2>
                            <div className="h-2 w-2 rounded-full bg-amber-400 animate-pulse" />
                        </div>
                        <div className="bg-white/20 border border-[#CCC5B9]/90 py-8 px-6 flex flex-col gap-5">
                            <div className="flex flex-col gap-1.5">
                                {loginError && (
                                    <div className="mb-6 p-3 bg-red-900/30 border border-red-700 rounded-lg">
                                        <p className="text-white text-sm">{loginError}</p>
                                    </div>
                                )}
                                <label className="text-sm font-semibold text-stone-700 tracking-wide">Username or E-Mail</label>
                                <input
                                    type="text"
                                    placeholder="Enter your username or email"
                                    required
                                    value={loginUsername}
                                    onChange={(e) => setLoginUsername(e.target.value)}
                                    className="w-full bg-amber-50/60 border border-stone-300 rounded-xl px-4 py-2.5 text-stone-800 placeholder-stone-400 outline-hidden focus:bg-amber-50 focus:border-stone-500 focus:ring-3 focus:ring-stone-500/20 transition-all duration-200"
                                />
                            </div>
                            <div className="flex flex-col gap-1.5">
                                <label className="text-sm font-semibold text-stone-700 tracking-wide">Password</label>
                                <input
                                    type="password"
                                    placeholder="••••••••"
                                    required
                                    value={loginPassword}
                                    onChange={(e) => setLoginPassword(e.target.value)}
                                    className="w-full bg-amber-50/60 border border-stone-300 rounded-xl px-4 py-2.5 text-stone-800 placeholder-stone-400 outline-hidden focus:bg-amber-50 focus:border-stone-500 focus:ring-3 focus:ring-stone-500/20 transition-all duration-200"
                                />
                            </div>
                            <button
                                type="submit"
                                disabled={loginLoading}
                                className="mt-2 w-full bg-slate-800 hover:bg-stone-900 text-amber-50 font-medium py-3 rounded-xl shadow-md cursor-pointer hover:shadow-lg active:scale-[0.99] transition-all duration-150">
                                {loginLoading ? (
                                    <>
                                        <div className="animate-spin rounded-full h-5 w-5 border-2 border-white border-t-transparent"></div>
                                        Signing In...
                                    </>
                                ) : (
                                    'Login'
                                )}
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default Account;
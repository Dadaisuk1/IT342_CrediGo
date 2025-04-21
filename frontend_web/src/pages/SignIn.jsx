import React, { useState } from 'react';
import backgroundGames from '../assets/background.svg';
import { FaGithub, FaGoogle, FaEye, FaEyeSlash, FaGamepad, FaLock, FaShieldAlt } from 'react-icons/fa';

const SignInPage = () => {
const [email, setEmail] = useState('');
const [password, setPassword] = useState('');
const [showPassword, setShowPassword] = useState(false);
const [rememberMe, setRememberMe] = useState(false);

const handleNavigation = (path) => {
    window.location.href = path;
};

const handleSubmit = (e) => {
    e.preventDefault();
    // Handle login logic here
};

return (
    <div
    className="min-h-screen overflow-hidden bg-fixed bg-cover bg-center relative"
    style={{ backgroundImage: `url(${backgroundGames})` }}
    >
    <div className="absolute inset-0 bg-black bg-opacity-60" />
        {/* Left Container */}
        <div className="relative z-10 flex items-center justify-between mx-8 md:mx-16 min-h-screen text-white">
            <div className="hidden md:block w-2/5 pr-8">
                <h1 className="text-4xl font-bold mb-6">CrediGo</h1>
                <h2 className="text-2xl font-semibold mb-8">Welcome back to your gaming wallet</h2>

                <div className="space-y-6">
                    <div className="flex items-start space-x-4">
                        <div div className="bg-[#eebbc3] p-3 rounded-full">
                            <FaGamepad className="text-[#232946]" size={24} />
                        </div>
                        <div>
                            <h3 className="font-bold text-xl mb-2">Ready to Play?</h3>
                            <p className="text-gray-300">Your gaming wallet is just a click away. Sign in to access all your funds.</p>
                        </div>
                    </div>
                    <div className="flex items-start space-x-4">
                        <div className="bg-[#eebbc3] p-3 rounded-full">
                            <FaShieldAlt className="text-[#232946]" size={24} />
                        </div>
                        <div>
                            <h3 className="font-bold text-xl mb-2">Secure Access</h3>
                            <p className="text-gray-300">Your account is protected with advanced security. Only you can access your wallet.</p>
                        </div>
                    </div>
                </div>

                <div className="mt-12">
                    <div className="flex items-center space-x-4 mb-6">
                    <div className="w-12 h-12 bg-[#eebbc3] rounded-full flex items-center justify-center">
                        <span className="text-[#232946] font-bold">1</span>
                    </div>
                    <div>
                        <h3 className="font-semibold text-lg">Sign In</h3>
                        <p className="text-gray-300 text-sm">Access your CrediGo account</p>
                    </div>
                    </div>

                    <div className="flex items-center space-x-4 mb-6">
                    <div className="w-12 h-12 bg-gray-700 rounded-full flex items-center justify-center">
                        <span className="text-white font-bold">2</span>
                    </div>
                    <div>
                        <h3 className="font-semibold text-lg">Choose Game</h3>
                        <p className="text-gray-300 text-sm">Select from your connected games</p>
                    </div>
                    </div>

                    <div className="flex items-center space-x-4">
                    <div className="w-12 h-12 bg-gray-700 rounded-full flex items-center justify-center">
                        <span className="text-white font-bold">3</span>
                    </div>
                    <div>
                        <h3 className="font-semibold text-lg">Make Purchase</h3>
                        <p className="text-gray-300 text-sm">Complete your transaction securely</p>
                    </div>
                    </div>
                </div>
            </div>

            {/* Form */}
            <div className="w-full md:w-3/5 max-w-lg mx-auto md:mx-0">
                <div className="w-full p-8 bg-[#232946] rounded-lg shadow-2xl border border-[#4a5294]">
                    <div className="text-center mb-8">
                    <FaLock size={40} className="mx-auto text-[#eebbc3] mb-4" />
                    <h2 className="text-2xl font-bold">Sign In to CrediGo</h2>
                    <p className="text-gray-300 mt-2">Access your universal gaming wallet</p>
                    </div>

                    <div className="flex items-center gap-5 mb-6">
                    <button className="flex items-center justify-center gap-2 border border-gray-300 py-2 w-full rounded-md hover:bg-[#2a2f4a] transition">
                        <FaGithub size={18} />
                        <span>GitHub</span>
                    </button>
                    <button className="flex items-center justify-center gap-2 border border-gray-300 py-2 w-full rounded-md hover:bg-[#2a2f4a] transition">
                        <FaGoogle size={18} />
                        <span>Google</span>
                    </button>
                    </div>

                    <div className="flex items-center gap-4 mb-6">
                    <hr className="flex-grow border-t border-gray-400" />
                    <span className="text-white text-xs">or</span>
                    <hr className="flex-grow border-t border-gray-400" />
                    </div>

                    <form onSubmit={handleSubmit} className="flex flex-col gap-5">
                    <div className="flex flex-col gap-2">
                        <label htmlFor="email">Email or Username</label>
                        <input
                        type="text"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="Enter your email or username"
                        className="p-3 rounded-md bg-[#232946] border border-white text-white focus:border-[#eebbc3] focus:outline-none"
                        required
                        />
                    </div>

                    <div className="flex flex-col gap-2">
                        <div className="flex justify-between">
                        <label htmlFor="password">Password</label>
                        <span
                            className="text-[#eebbc3] text-sm cursor-pointer hover:underline"
                            onClick={() => handleNavigation('/forgot-password')}
                        >
                            Forgot password?
                        </span>
                        </div>
                        <div className="relative">
                        <input
                            type={showPassword ? 'text' : 'password'}
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="Enter your password"
                            className="p-3 pr-10 rounded-md bg-[#232946] border border-white text-white w-full focus:border-[#eebbc3] focus:outline-none"
                            required
                        />
                        <span
                            className="absolute top-1/2 right-3 -translate-y-1/2 cursor-pointer"
                            onClick={() => setShowPassword(!showPassword)}
                        >
                            {showPassword ? <FaEyeSlash size={18} /> : <FaEye size={18} />}
                        </span>
                        </div>
                    </div>

                    <div className="flex items-center">
                        <input
                        id="remember-me"
                        type="checkbox"
                        checked={rememberMe}
                        onChange={() => setRememberMe(!rememberMe)}
                        className="w-4 h-4 border border-gray-300 rounded bg-[#232946] focus:ring-3 focus:ring-[#eebbc3]"
                        />
                        <label htmlFor="remember-me" className="ml-2 text-sm text-gray-300">
                        Keep me signed in on this device
                        </label>
                    </div>

                    <button
                        type="submit"
                        className="bg-[#eebbc3] text-[#232946] font-semibold py-3 rounded-md hover:bg-[#ffd5dc] transition mt-2"
                    >
                        Sign In to My Wallet
                    </button>

                    <div className="bg-[#2a325a] p-4 rounded-md mt-2">
                        <p className="text-sm text-center text-white">
                        <span className="text-[#eebbc3] font-semibold">New!</span> Special event bonuses available for limited time!
                        <span className="block mt-1 text-xs">Sign in to view exclusive offers</span>
                        </p>
                    </div>

                    <p className="text-sm text-center text-gray-300 mt-4">
                        Don't have an account yet?
                        <span
                        onClick={() => handleNavigation('/sign-up')}
                        className="ml-1 text-[#eebbc3] font-semibold cursor-pointer hover:underline"
                        >
                        Create one now
                        </span>
                    </p>
                    </form>
                </div>

                <div className="mt-8 text-center">
                    <p className="text-sm text-gray-400">
                    Need help? <span className="text-[#eebbc3] cursor-pointer hover:underline">Contact Support</span>
                    </p>
                </div>
            </div>
        </div>
    </div>
);
};

export default SignInPage;

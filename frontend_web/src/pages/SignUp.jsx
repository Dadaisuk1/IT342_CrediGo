import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import pattern_bg from '../assets/pattern-bg.svg';
import { IoMdArrowRoundBack, IoIosEye, IoMdEyeOff } from "react-icons/io";
import { FaGithub } from 'react-icons/fa';

const SignUp = () => {
    const location = useLocation();
    const navigate = useNavigate();
    
    // Path states
    const isSignIn = location.pathname === '/sign-in';
    const isSignUp = location.pathname === '/sign-up';
    
    // Form states
    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [dob, setDob] = useState('');
    const [password, setPassword] = useState(''); // Added missing password state
    const [confirmPassword, setConfirmPassword] = useState('');
    
    // UI states
    const [isAgree, setIsAgree] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        // Basic validation
        if (!email || !username || !password || !confirmPassword) {
            setError('Please fill in all required fields');
            return;
        }

        if (password !== confirmPassword) {
            setError('Passwords do not match');
            return;
        }

        setIsLoading(true);

        try {
            const res = await fetch("http://localhost:8080/api/users/createUser", {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    email, 
                    username, 
                    phonenumber: phoneNumber, 
                    dateofbirth: dob, 
                    password, 
                    status: "active", 
                    wallet: '0.00', 
                    role: "Customer" 
                }),
            });

            const text = await res.text();
            let data;

            try {
                data = JSON.parse(text);
            } catch {
                data = { message: text || 'Something went wrong.' };
            }

            if (res.ok) {
                alert('Signed up successfully!');
                navigate('/sign-in');
            } else {
                setError(data.message || 'Sign up failed.');
            }
        } catch (err) {
            setError('Error connecting to server');
            console.error(err);
        } finally {
            setIsLoading(false);
        }
    };

    const handleNavigation = (path) => {
        navigate(path);
    };

    return (
        <div className="flex h-full scroll-smooth bg-[#fffffe]">
            {/* Left side with image */}
            <div className="hidden md:block md:w-3/5 h-full">
                <img src={pattern_bg} alt="Background pattern" className="w-full h-full object-cover" />
            </div>

            {/* Right side content */}
            <div className="w-full md:w-2/5 flex flex-col">
                {/* Back to home */}
                <div
                    className="text-right w-full px-6 py-4 flex gap-1 items-center cursor-pointer"
                    onClick={() => handleNavigation('/home')}
                >
                    <IoMdArrowRoundBack size={20} color="#232946" />
                    <p className="text-[#232946] text-sm cursor-pointer font-medium">Back to Home</p>
                </div>
                
                {/* Breadcrumbs */}
                <div className="flex justify-center mb-5">
                    {/* Sign In */}
                    <p 
                        onClick={() => handleNavigation('/sign-in')}
                        className={`p-5 text-xl cursor-pointer ${isSignIn ? 'border-b-4 border-[#232946] font-medium' : 'border-b border-gray-300'}`}
                    >
                        Sign in
                    </p>
                    {/* Sign Up */}
                    <p
                        onClick={() => handleNavigation('/sign-up')}
                        className={`p-5 text-xl cursor-pointer ${isSignUp ? 'border-b-4 border-[#232946] font-medium' : 'border-b border-gray-300'}`}
                    >
                        Sign up
                    </p>
                </div>

                {/* Form Section */}
                <div className="flex justify-center items-center flex-grow px-4">
                    <div className="bg-[#232946] w-full max-w-md p-6 rounded-lg shadow-lg">
                        <h1 className="text-2xl md:text-3xl font-bold text-center mb-6 text-white">Sign Up</h1>

                        {error && (
                            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
                                {error}
                            </div>
                        )}

                        <form className="flex flex-col gap-4" onSubmit={handleSubmit}>
                            {/* Email */}
                            <div className="flex flex-col gap-1">
                                <label className="text-base text-white font-medium">Email</label>
                                <input
                                    type="email"
                                    placeholder="Enter your email"
                                    className="p-3 border border-gray-300 rounded-md text-sm w-full"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    required
                                />
                            </div>

                            {/* Username */}
                            <div className="flex flex-col gap-1">
                                <label className="text-base text-white font-medium">Username</label>
                                <input
                                    type="text"
                                    placeholder="Enter your username"
                                    className="p-3 border border-gray-300 rounded-md text-sm w-full"
                                    value={username}
                                    onChange={(e) => setUsername(e.target.value)}
                                    required
                                />
                            </div>

                            {/* Phone Number (Optional) */}
                            <div className="flex flex-col gap-1">
                                <label className="text-base text-white font-medium">Phone Number (Optional)</label>
                                <input
                                    type="tel"
                                    placeholder="Enter your phone number"
                                    className="p-3 border border-gray-300 rounded-md text-sm w-full"
                                    value={phoneNumber}
                                    onChange={(e) => setPhoneNumber(e.target.value)}
                                />
                            </div>

                            {/* Date of Birth (Optional) */}
                            <div className="flex flex-col gap-1">
                                <label className="text-base text-white font-medium">Date of Birth (Optional)</label>
                                <input
                                    type="date"
                                    className="p-3 border border-gray-300 rounded-md text-sm w-full"
                                    value={dob}
                                    onChange={(e) => setDob(e.target.value)}
                                />
                            </div>

                            {/* Password */}
                            <div className="flex flex-col gap-1">
                                <label className="text-base text-white font-medium">Password</label>
                                <div className="relative">
                                    <input
                                        type={showPassword ? 'text' : 'password'}
                                        placeholder="Enter your password"
                                        className="p-3 pr-10 border border-gray-300 rounded-md text-sm w-full"
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        required
                                    />
                                    <span 
                                        className="absolute top-1/2 right-3 -translate-y-1/2 cursor-pointer" 
                                        onClick={() => setShowPassword(prev => !prev)}
                                    >  
                                        {showPassword ? <IoMdEyeOff size={20} color="#232946" /> : <IoIosEye size={20} color="#232946" />}
                                    </span>
                                </div>
                            </div>

                            {/* Re-enter password */}
                            <div className="flex flex-col gap-1">
                                <label className="text-base text-white font-medium">Confirm Password</label>
                                <div className="relative">
                                    <input
                                        type={showConfirmPassword ? 'text' : 'password'}
                                        placeholder="Confirm your password"
                                        className="p-3 pr-10 border border-gray-300 rounded-md text-sm w-full"
                                        value={confirmPassword}
                                        onChange={(e) => setConfirmPassword(e.target.value)}
                                        required
                                    />
                                    <span
                                        className="absolute top-1/2 right-3 -translate-y-1/2 cursor-pointer"
                                        onClick={() => setShowConfirmPassword(prev => !prev)}
                                    >
                                        {showConfirmPassword ? <IoMdEyeOff size={20} color="#232946" /> : <IoIosEye size={20} color="#232946" />}
                                    </span>
                                </div>
                            </div>
                            
                            {/* Terms and Condition */}
                            <div className="flex items-start gap-2 text-xs text-gray-300">
                                <input 
                                    type="checkbox" 
                                    checked={isAgree} 
                                    onChange={() => setIsAgree(!isAgree)}
                                    id="terms"
                                    className="mt-1"
                                />
                                <label htmlFor="terms">I agree to the Terms and Conditions</label>
                            </div>

                            {/* Submit Button */}
                            <button
                                type="submit"
                                disabled={!isAgree || isLoading}
                                className={`text-sm font-medium py-3 rounded-md transition ${
                                    isAgree && !isLoading
                                        ? 'bg-[#6285cf] text-[#fffffe] hover:bg-[#445ab1]'
                                        : 'bg-[#e2eaf7] text-gray-700 cursor-not-allowed'
                                }`}
                            >
                                {isLoading ? 'Signing Up...' : 'Sign Up'}
                            </button>
                        </form>

                        <p className="text-center text-xs text-gray-300 mt-5 mb-5">or sign up with</p>

                        <button className="flex items-center justify-center gap-2 border border-gray-300 py-3 w-full rounded-md">
                            <FaGithub size={20} color="#FFFFFE" />
                            <span className="text-white">Sign up with GitHub</span>
                        </button>

                        <div className="mt-6 text-center text-xs text-gray-300">
                            <p className="inline-block">You already have an account?</p>
                            <span 
                                onClick={() => handleNavigation('/sign-in')}
                                className="ml-2 text-white font-semibold cursor-pointer hover:underline"
                            >
                                Sign in here
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default SignUp;
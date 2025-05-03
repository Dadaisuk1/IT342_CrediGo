import React,{ useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import pattern_bg from '../assets/pattern-bg.svg';
import { IoMdArrowRoundBack, IoIosEye, IoMdEyeOff } from "react-icons/io";
import { FaGithub } from 'react-icons/fa';
import { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import { jwtDecode } from "jwt-decode";
import toast from 'react-hot-toast';


const SignIn = () => {

    const location = useLocation();
    const navigate = useNavigate();
    const isSignIn = location.pathname === '/sign-in'; // Router Path
    const isSignUp = location.pathname === '/sign-up'; // Router Path
    const [showPassword, setShowPassword] = useState(false); // Hide and Unhide Password
    // // Add these at the top inside your component:
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const { login } = useContext(AuthContext);

    
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
          const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
            const res = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password }),
          });
      
          if (res.ok) {
            const data = await res.json(); // ✅ only json, don't parse manually
            console.log("Login response:", data);
      
            localStorage.setItem('token', data.token);
            localStorage.setItem('userid', data.userid);
            localStorage.setItem('username', data.username);
      
            login(data.token); // update auth context
      
            const decoded = jwtDecode(data.token);
            console.log("Decoded JWT:", decoded);
      
            const isAdmin = decoded?.sub === 'jose'; 
            if (isAdmin) {
              navigate('/admin');
            } else {
              navigate('/home');
            }
          } else {
            const errorText = await res.text(); // read error manually if fail
            toast.error(errorText || 'Sign in failed!');
          }
        } catch (err) {
          toast.error('An error occurred. Please try again.');
          console.error(err);
        }
      };
      
      
    

    const handleNavigation = (path) => {
        navigate(path);
    };

    return (
        <div className="flex w-screen h-screen bg-[#fff]">
            {/* Left side with image */}
            <div className="w-[60%] h-full">
                <img src={pattern_bg} alt="60%-background" className="w-full h-full object-cover" />
            </div>

            {/* Right side content */}
            <div className="w-[40%] flex flex-col mt-10 mb-10">
                {/* Back to home */}
                <div
                    className="text-right w-full px-6 py-4 flex gap-1 items-center cursor-pointer"
                    onClick={() => handleNavigation('/')}
                >
                    <IoMdArrowRoundBack size={20} color="#232946" />
                    <p className="text-[#232946] text-[16.67px] cursor-pointer font-medium">Back to Home</p>
                </div>

                {/* Breadcrumbs */}
                <div className='flex justify-center'>
                    {/* Sign In */}
                    <p 
                        onClick={() => handleNavigation('/sign-in')}
                        className={`p-5 text-[24px] cursor-pointer ${isSignIn ? 'border-b-4 border-[#232946] font-medium' : 'border-b border-gray-300'}`}>Sign in</p>
                    {/* Sign Up */}
                    <p
                        onClick={() => handleNavigation('/sign-up')}
                        className={`p-5 text-[24px] cursor-pointer ${isSignUp ? 'border-b-4 border-[#232946] font-medium' : 'border-b border-gray-300'}`}>Sign up</p>
                </div>

                {/* Form Section */}
                <div className="flex justify-center items-center flex-grow">
                    <div className="bg-[#232946] h-[600px] w-[80%] max-w-[600px] max-h-[700px] p-6 rounded-lg shadow-lg">
                        <h1 className="text-[32px] font-bold text-center mb-6 text-[#FFFFFE]">Sign In</h1>

                        <form className="flex flex-col gap-6" onSubmit={handleSubmit}>
                            <div className="flex flex-col gap-1">
                                <label className="text-[18px] text-white font-medium">Username</label>
                                <input
                                    type="text"
                                    value={username}
                                    onChange={(e) => setUsername(e.target.value)}
                                    placeholder="Enter your username"
                                    className="p-3 border border-gray-300 rounded-md text-[15px]"
                                    required
                                />
                            </div>
                            <div className="flex flex-col gap-1">
                                <label className="text-[18px] text-white font-medium">Password</label>
                                <div className='relative'>
                                    <input
                                        type={showPassword ? 'text' : 'password'}
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        
                                        placeholder="Enter your password"
                                        className="p-3 pr-10 border border-gray-300 rounded-md focus:outline-none text-[15px] w-full"
                                        required
                                    />
                                    <span
                                        className="absolute top-1/2 right-3 -translate-y-1/2 cursor-pointer"  
                                        onClick={() => setShowPassword(prev => !prev)}
                                    >
                                        {showPassword ? <IoMdEyeOff size={25} color='#232946' /> : <IoIosEye size={25} color='#232946' />}
                                    </span>
                                </div>
                            </div>
                            <div className="text-left text-sm text-gray-300">
                                <span className="cursor-pointer hover:underline">You forgot your email or password?</span>
                            </div>

                            <button
                                type="submit"
                                className="bg-[#F3C6CD] text-[#232946] text-[15px] font-medium py-3 rounded-md hover:bg-[#E7909E]] transition"
                            >
                                Sign In
                            </button>
                        </form>

                        <p className="text-center text-[13px] text-gray-300 mt-5 mb-5">or sign in with</p>

                        <button className="flex items-center justify-center gap-2 border border-gray-300 py-4 w-full mt-2 rounded-md">
                            <FaGithub size={20} color='#FFFFFE' />
                            <span className='text-[#fffffe]'>Sign in with GitHub</span>
                        </button>

                        <div className="mt-6 text-center text-[12px] text-gray-300">
                            <p className="inline-block">Don't have an account?</p>
                            <span 
                                onClick={() => handleNavigation('/sign-up')}
                                className="ml-2 text-[#ffffff] font-semibold cursor-pointer hover:underline">
                                Sign up here
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default SignIn;

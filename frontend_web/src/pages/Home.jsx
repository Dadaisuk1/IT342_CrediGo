import React, { lazy } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

const Navbar = lazy(() => import('../components/layout/Navbar'));

const Home = () => {
    const location = useLocation();
    const navigate = useNavigate();

    const isSignIn = location.pathname === '/sign-in';
    const isSignUp = location.pathname === '/sign-up';

    const handleNavigation = (path) => {
        navigate(path);
    };

    return (
        <div className="min-h-screen bg-[#232946] text-white">
        {/* Navbar always on top */}
        <Navbar />

        {/* Main content centered */}
        <div className="flex flex-col items-center justify-center h-[80vh] gap-6">
            <p className="text-[32px] font-semibold">Welcome Home!</p>
        </div>
        </div>
    );
};

export default Home;

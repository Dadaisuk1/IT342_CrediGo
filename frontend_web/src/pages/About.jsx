import React from "react";
import Navbar from "../components/layout/Navbar";

const About = () => {
    return (
        <>
            <Navbar />
            <div className="flex flex-col items-center justify-center h-[80vh] gap-6">
                <p className="text-[32px] font-semibold text-white">About</p>
            </div>
        </>
    )
}

export default About;

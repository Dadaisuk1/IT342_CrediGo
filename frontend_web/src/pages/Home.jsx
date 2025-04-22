import React from "react";
import Navbar from "../components/layout/Navbar";
import FeatureCard from "../components/FeatureCard";
import Button from "../components/layout/Button"; // Import Button
import logo from "../assets/images/credigo_icon.svg";

// --- Data Arrays ---
// Define feature data - makes it easy to add/remove/edit features
const featuresData = [
    {
        title: "Digital Funds at Your Fingertips",
        description:
            "Browse and select from a wide range of digital funds for popular online games and platforms.",
        // icon: <YourIconComponent /> // Example: Add icons later
    },
    {
        title: "Track Your Spending",
        description:
            "Easily view your past transactions to stay on top of your gaming budget.",
    },
    {
        title: "Save to Your Wishlist",
        description:
            "Keep track of the digital funds you're interested in for future purchases.",
    },
    {
        title: "Rate and Review",
        description:
            "Share your experiences by rating and reviewing digital funds after you buy them.",
    },
    {
        title: "Your Secure E-Wallet",
        description:
            "Store funds securely within your in-app wallet for faster and smoother transactions.",
    },
    {
        title: "Find What You Need Instantly",
        description:
            "Quickly search for digital funds by name or the game you play.",
    },
];

// Define game data (use actual image paths or URLs when available)
const gamesData = [
    {
        src: "https://via.placeholder.com/80/007bff/ffffff?Text=Valorant",
        alt: "Valorant",
    },
    {
        src: "https://via.placeholder.com/80/28a745/ffffff?Text=Dota%202",
        alt: "Dota 2",
    },
    {
        src: "https://via.placeholder.com/80/dc3545/ffffff?Text=Steam",
        alt: "Steam",
    },
    {
        src: "https://via.placeholder.com/80/ffc107/000000?Text=Mobile%20Legends",
        alt: "Mobile Legends",
    },
    {
        src: "https://via.placeholder.com/80/17a2b8/ffffff?Text=Fortnite",
        alt: "Fortnite",
    },
    {
        src: "https://via.placeholder.com/80/6610f2/ffffff?Text=PlayStation",
        alt: "PlayStation",
    },
    // Add more games as needed
];
// --- End Data Arrays ---

const HomePage = () => {
    return (
        // Using theme colors
        <div className="min-h-screen bg-primary-medium text-text-light">
            <Navbar /> {/* Assuming Navbar is styled appropriately */}
            {/* Use <main> for the main content area */}
            <main>
                {/* Hero Section */}
                {/* Using theme colors and gradient */}
                <section className="py-20 md:py-32 bg-gradient-to-br from-primary-medium to-primary-dark">
                    <div className="container mx-auto text-center px-4">
                        {" "}
                        {/* Added horizontal padding */}
                        <img
                            src={logo}
                            alt="CrediGo Logo"
                            className="w-24 h-24 mx-auto mb-6"
                        />
                        <h1 className="text-4xl md:text-5xl font-bold text-text-light mb-4 tracking-wide">
                            The Simple Way to Fuel Your Game
                        </h1>
                        <p className="text-lg text-text-muted mb-8 max-w-2xl mx-auto">
                            {" "}
                            {/* Constrained width */}
                            Secure microtransactions, easy wallet management, and the best
                            deals for your favorite games.
                        </p>
                        {/* Using the reusable Button component */}
                        <Button to="/sign-up">Get Started for Free</Button>
                    </div>
                </section>

                {/* Features Showcase */}
                <section className="py-16 bg-primary-light">
                    <div className="container mx-auto text-center px-4">
                        {" "}
                        {/* Added horizontal padding */}
                        <h2 className="text-3xl font-semibold text-text-light mb-12">
                            {" "}
                            {/* Increased bottom margin */}
                            Explore CrediGo's Powerful Features
                        </h2>
                        {/* Render FeatureCards dynamically from data */}
                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                            {featuresData.map((feature, index) => (
                                <FeatureCard
                                    key={index} // Use a unique ID if available, otherwise index (less ideal for dynamic lists)
                                    title={feature.title}
                                    description={feature.description}
                                    icon={feature.icon} // Pass icon if defined
                                />
                            ))}
                        </div>
                    </div>
                </section>

                {/* Available Games/Platforms */}
                <section className="py-16 bg-primary-medium">
                    <div className="container mx-auto text-center px-4">
                        {" "}
                        {/* Added horizontal padding */}
                        <h2 className="text-3xl font-semibold text-text-light mb-8">
                            Explore Funds for Your Favorite Games
                        </h2>
                        {/* Render game logos dynamically */}
                        <div className="flex flex-wrap justify-center items-center gap-4 md:gap-6">
                            {gamesData.map((game, index) => (
                                <img
                                    key={index} // Use unique ID if available
                                    src={game.src}
                                    alt={game.alt}
                                    className="h-16 w-auto md:h-20 object-contain rounded-md shadow-md bg-white p-1" // Added background for visibility & padding
                                />
                            ))}
                        </div>
                        <p className="mt-8 text-text-muted">
                            {" "}
                            {/* Increased top margin */}
                            Log in to see the full catalog and start buying!
                        </p>
                    </div>
                </section>

                {/* Call to Action */}
                <section className="py-16 bg-primary-light">
                    <div className="container mx-auto text-center px-4">
                        {" "}
                        {/* Added horizontal padding */}
                        <h2 className="text-3xl font-semibold text-text-light mb-6">
                            Ready to Enhance Your Gaming Experience?
                        </h2>
                        {/* Using the reusable Button component */}
                        <Button to="/sign-up">Join CrediGo Today!</Button>
                    </div>
                </section>
            </main>{" "}
            {/* End main content */}
            {/* Footer */}
            {/* Using theme color */}
            <footer className="py-8 bg-primary-dark text-center text-text-muted">
                <div className="container mx-auto px-4">
                    {" "}
                    {/* Added horizontal padding */}
                    <p>&copy; {new Date().getFullYear()} CrediGo. All rights reserved.</p>
                    {/* Optional: Links to terms, privacy policy, etc. */}
                    {/* <div className="mt-2">
                        <a href="/terms" className="text-sm hover:text-text-light transition mx-2">Terms of Service</a>
                        <a href="/privacy" className="text-sm hover:text-text-light transition mx-2">Privacy Policy</a>
                    </div> */}
                </div>
            </footer>
        </div>
    );
};

export default HomePage;

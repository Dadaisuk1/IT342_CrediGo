import React from 'react';
import { Link } from 'react-router-dom';
import {
  Search,
  Heart,
  ShoppingCart,
  User,
  ChevronLeft,
  ChevronRight,
  Zap,
  Shield,
  Headphones,
  Wallet,
} from 'lucide-react';

const Homepage = () => {
  return (
    <div className="flex flex-col min-h-screen">
    

      <main className="flex-1">
        {/* Hero Section */}
        <section className="grid grid-cols-1 md:grid-cols-2">
          <div className="bg-green-600 p-8 md:p-12 lg:p-16 flex flex-col justify-center">
            <h1 className="text-4xl font-bold text-white mb-4">Top Up Your Gaming Experience</h1>
            <p className="text-white mb-6">Instant transactions for Steam, Riot, Garena, and more!</p>
            <div className="flex gap-4">
              <Link to="/shop" className="bg-orange-500 hover:bg-orange-600 text-white px-4 py-2 rounded font-semibold">Shop Now</Link>
              <button className="bg-white text-black px-4 py-2 rounded font-semibold border border-gray-200">Learn More</button>
            </div>
          </div>
          <div className="relative h-[300px] md:h-auto bg-gray-200 flex items-center justify-center">
            <span className="text-gray-500">[Image Placeholder]</span>
            <div className="absolute inset-0 flex items-center justify-between p-4">
              <ChevronLeft className="h-10 w-10 text-white bg-black/20 rounded-full p-2 cursor-pointer" />
              <ChevronRight className="h-10 w-10 text-white bg-black/20 rounded-full p-2 cursor-pointer" />
            </div>
          </div>
        </section>

        {/* Popular Game Points */}
        <section className="py-12 px-4 md:px-6">
          <h2 className="text-2xl font-bold mb-8">Popular Game Points</h2>
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-6">
            {['Steam', 'Riot', 'Garena', 'Xbox', 'Mobile Legends'].map((title, index) => (
              <div key={index} className="p-4 border rounded shadow-sm">
                <h3 className="text-sm font-medium">{title}</h3>
                <div className="h-[100px] bg-gray-200 my-2 rounded" />
                <h4 className="font-medium">{title} Code</h4>
                <div className="text-yellow-400 text-sm">★★★★☆</div>
                <div className="flex justify-between items-center mt-2">
                  <span className="text-green-600 font-bold">$20.00</span>
                  <button className="bg-green-500 text-white px-3 py-1 rounded text-sm">Add to Cart</button>
                </div>
              </div>
            ))}
          </div>
        </section>

        {/* Why Choose Us */}
        <section className="py-12 bg-gray-100">
          <h2 className="text-2xl font-bold text-center mb-12">Why Choose Us</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8 px-4 md:px-6">
            {[{
              icon: <Zap className="h-6 w-6" />, title: 'Instant Transaction', description: 'Get your points instantly after transaction'
            }, {
              icon: <Shield className="h-6 w-6" />, title: 'Secure Payment', description: 'Protected with encryption'
            }, {
              icon: <Headphones className="h-6 w-6" />, title: '24/7 support', description: 'Support team always ready'
            }, {
              icon: <Wallet className="h-6 w-6" />, title: 'E-Wallet Ready', description: 'Faster checkout experience'
            }].map((item, index) => (
              <div key={index} className="text-center flex flex-col items-center">
                <div className="h-16 w-16 rounded-full bg-green-100 flex items-center justify-center mb-4">
                  <div className="text-green-600">{item.icon}</div>
                </div>
                <h3 className="font-bold mb-2">{item.title}</h3>
                <p className="text-sm text-gray-600">{item.description}</p>
              </div>
            ))}
          </div>
        </section>
      </main>

      {/* Footer */}
      <footer className="bg-green-800 text-white py-12 px-4 md:px-6">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          <div>
            <h3 className="font-bold mb-4">CrediGo</h3>
            <p className="text-sm">Your trusted source of game points since 2025</p>
          </div>
          <div>
            <h3 className="font-bold mb-4">Quick Links</h3>
            <ul className="space-y-2 text-sm">
              <li><Link to="/about">About Us</Link></li>
              <li><Link to="/contact">Contact</Link></li>
              <li><Link to="/faqs">FAQs</Link></li>
              <li><Link to="/terms">Terms</Link></li>
            </ul>
          </div>
          <div>
            <h3 className="font-bold mb-4">Contact Us</h3>
            <p className="text-sm">support.credigo@gmail.com</p>
            <p className="text-sm">+63992345238</p>
          </div>
          <div>
            <h3 className="font-bold mb-4">Follow Us</h3>
            <p className="text-sm">Social links here...</p>
          </div>
        </div>
        <div className="text-center text-sm mt-8 border-t border-white/20 pt-4">
          <p>&copy; 2025 CrediGo. All Rights Reserved.</p>
        </div>
      </footer>
    </div>
  );
};

export default Homepage;

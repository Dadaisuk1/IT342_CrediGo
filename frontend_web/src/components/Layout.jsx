import React from 'react';
import { Outlet, Link } from 'react-router-dom';
import {
  Search, Heart, ShoppingCart, User,
} from 'lucide-react';

const Layout = () => {
  return (
    <div className="flex flex-col min-h-screen">
      <header className="sticky top-0 z-50 w-full border-b bg-white">
        <div className="flex items-center justify-between h-16 px-4 md:px-6">
          <div className="flex items-center gap-6">
            <Link to="/" className="flex items-center">
              <div className="h-10 w-32 bg-green-600 rounded-full border-2 border-black flex items-center justify-center">
                <span className="text-white font-bold text-xl">CrediGo</span>
              </div>
            </Link>
            <nav className="flex gap-6">
              <Link to="/home" className="font-medium">Home</Link>
              <Link to="/shop" className="font-medium">Shop</Link>
              <Link to="/top-up" className="font-medium">Top Up</Link>
              <Link to="/wishlist" className="font-medium">WishList</Link>
            </nav>
          </div>
          <div className="flex items-center gap-4">
            <div className="font-medium">Wallet(<span className="text-red-500">0.00</span>)</div>
            <div className="relative max-w-sm w-full">
              <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-gray-500" />
              <input type="search" placeholder="Search..." className="pl-8 pr-4 py-2 border rounded-full w-full" />
            </div>
            <Heart className="h-5 w-5 cursor-pointer" />
            <ShoppingCart className="h-5 w-5 cursor-pointer" />
            <User className="h-5 w-5 cursor-pointer" />
          </div>
        </div>
      </header>

      <main className="flex-1">
        <Outlet /> {/* <- This is where your page (Shop, TopUp, etc.) will render */}
      </main>

      <footer className="bg-green-800 text-white py-12 px-4 md:px-6">
        <p className="text-center text-sm">&copy; 2025 CrediGo. All Rights Reserved.</p>
      </footer>
    </div>
  );
};

export default Layout;

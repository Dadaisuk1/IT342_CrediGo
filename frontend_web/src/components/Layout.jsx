import React, { useState, useEffect } from 'react';
import { Outlet, Link, useNavigate } from 'react-router-dom';
import { Search, Heart, ShoppingCart, User, Mail, LogOut } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import toast from 'react-hot-toast';

const Layout = () => {
  const { wallet, mails, refetchWalletAndMails } = useAuth();
  const [showMails, setShowMails] = useState(false);
  const [showCart, setShowCart] = useState(false);
  const [cartItems, setCartItems] = useState([]);
  const [username, setUsername] = useState('');
  const navigate = useNavigate();

  const toggleMailDropdown = () => {
    setShowMails(!showMails);
  };

  const toggleCartDropdown = () => {
    const storedCart = JSON.parse(localStorage.getItem('cart')) || [];
    setCartItems(storedCart);
    setShowCart(!showCart);
  };

  const handleLogout = () => {
    localStorage.clear();
    toast.success('Logged out!');
    navigate('/sign-in', { replace: true }); // 🚀 Soft-refresh
  };

  const handleRemoveFromCart = (index) => {
    const updatedCart = [...cartItems];
    updatedCart.splice(index, 1);
    setCartItems(updatedCart);
    localStorage.setItem('cart', JSON.stringify(updatedCart));
    toast.success('Removed from cart.');
  };

  const handleCheckout = async () => {
    const token = localStorage.getItem('token');
    if (!token) {
      toast.error('Please login first.');
      return;
    }

    try {
      for (const item of cartItems) {
        const res = await fetch(`https://it342-credigo-msd3.onrender.com/api/buy/${item.productid}`, {
          method: 'POST',
          headers: { Authorization: `Bearer ${token}` },
        });
        const data = await res.json();
        if (!res.ok) {
          throw new Error(data.message || 'Failed to buy product.');
        }
      }
      toast.success('Checkout successful! 🎉');
      localStorage.removeItem('cart');
      setCartItems([]);
      setShowCart(false);
      refetchWalletAndMails(); // 💸 Refresh wallet after checkout
    } catch (error) {
      console.error('Checkout error:', error);
      toast.error('Checkout failed.');
    }
  };

  useEffect(() => {
    const storedCart = JSON.parse(localStorage.getItem('cart')) || [];
    setCartItems(storedCart);

    const storedUsername = localStorage.getItem('username');
    if (storedUsername) {
      setUsername(storedUsername);
    }
  }, []);

  const totalAmount = cartItems.reduce((sum, item) => sum + (item.salePrice || item.price), 0);

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
              <Link to="/profile" className="font-medium">Profile</Link>
            </nav>
          </div>

          <div className="flex items-center gap-4 relative">
            <div className="font-medium">
              Wallet (<span className="text-green-600">${wallet}</span>)
            </div>

            {/* Mail Dropdown */}
            <div className="relative">
              <Mail onClick={toggleMailDropdown} className="h-5 w-5 cursor-pointer" />
              {showMails && (
                <div className="absolute right-0 mt-2 w-64 bg-white shadow-lg rounded-md border p-4 z-50">
                  <h3 className="font-bold mb-2">Mails</h3>
                  {mails.length === 0 ? (
                    <p className="text-gray-500 text-sm">No mails</p>
                  ) : (
                    <ul className="space-y-2 max-h-60 overflow-y-auto">
                      {mails.map((mail) => (
                        <li key={mail.mailid} className="text-sm border-b pb-2">
                          <strong>{mail.subject}</strong><br />
                          <small className="text-gray-500">{new Date(mail.createdDate).toLocaleString()}</small>
                        </li>
                      ))}
                    </ul>
                  )}
                </div>
              )}
            </div>

            {/* Search */}
            <div className="relative max-w-sm w-full hidden md:block">
              <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-gray-500" />
              <input type="search" placeholder="Search..." className="pl-8 pr-4 py-2 border rounded-full w-full" />
            </div>

            {/* Wishlist */}
            <Link to="/wishlist">
              <Heart className="h-5 w-5 cursor-pointer" />
            </Link>

            {/* Cart Dropdown */}
            <div className="relative">
              <ShoppingCart onClick={toggleCartDropdown} className="h-5 w-5 cursor-pointer" />
              {cartItems.length > 0 && (
                <span className="absolute -top-2 -right-2 bg-red-600 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">
                  {cartItems.length}
                </span>
              )}
              {showCart && (
                <div className="absolute right-0 mt-2 w-72 bg-white shadow-lg rounded-md border p-4 z-50">
                  <h3 className="font-bold mb-2">Cart</h3>
                  {cartItems.length === 0 ? (
                    <p className="text-gray-500 text-sm">Cart is empty</p>
                  ) : (
                    <ul className="space-y-2 max-h-60 overflow-y-auto">
                      {cartItems.map((item, index) => (
                        <li key={index} className="flex justify-between items-center text-sm border-b pb-2">
                          <div>
                            <div>{item.productname}</div>
                            <div className="text-green-600 font-bold">${item.salePrice || item.price}</div>
                          </div>
                          <button
                            onClick={() => handleRemoveFromCart(index)}
                            className="text-red-500 hover:underline text-xs"
                          >
                            Remove
                          </button>
                        </li>
                      ))}
                    </ul>
                  )}
                  {cartItems.length > 0 && (
                    <>
                      <div className="font-semibold text-right mt-3">Total: ${totalAmount.toFixed(2)}</div>
                      <button
                        onClick={handleCheckout}
                        className="mt-3 w-full bg-blue-600 hover:bg-blue-700 text-white py-2 rounded-md text-sm font-semibold"
                      >
                        Checkout
                      </button>
                    </>
                  )}
                </div>
              )}
            </div>

            {/* Username & Logout */}
            <div className="flex items-center gap-2">
              <User className="h-5 w-5" />
              {username && <span className="text-sm font-medium">{username}</span>}
              <button onClick={handleLogout} title="Logout">
                <LogOut className="h-5 w-5 text-red-500" />
              </button>
            </div>
          </div>
        </div>
      </header>

      <main className="flex-1 bg-white">
        <Outlet />
      </main>

      <footer className="bg-green-800 text-white py-12 px-4 md:px-6">
        <p className="text-center text-sm">&copy; 2025 CrediGo. All Rights Reserved.</p>
      </footer>
    </div>
  );
};

export default Layout;

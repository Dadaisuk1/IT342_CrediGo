import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { ChevronLeft, ChevronRight, Zap, Shield, Headphones, Wallet } from 'lucide-react';

const Homepage = () => {
  const [products, setProducts] = useState([]);

  const fetchProducts = async () => {
    try {
      const res = await fetch('https://it342-credigo-msd3.onrender.com/api/products/getActiveProducts', {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      });
      const data = await res.json();
      setProducts(data.slice(0, 3)); // 👉 Only take the first 3 products
    } catch (error) {
      console.error('Failed to fetch products:', error);
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

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

        {/* Featured Products */}
        <section className="py-12 px-4 md:px-6">
          <h2 className="text-2xl font-bold mb-8">Featured Products</h2>
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
            {products.map((product) => (
              <div key={product.productid} className="bg-white p-4 rounded shadow">
                <img src={product.imageUrl || 'https://via.placeholder.com/150'} alt={product.productname} className="w-full h-40 object-cover mb-4" />
                <h3 className="text-lg font-semibold">{product.productname}</h3>
                <p className="text-gray-600 text-sm">{product.description}</p>
                <p className="text-green-600 font-bold mt-2">${product.salePrice || product.price}</p>
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
          {/* Footer content here */}
        </div>
        <div className="text-center text-sm mt-8 border-t border-white/20 pt-4">
          <p>&copy; 2025 CrediGo. All Rights Reserved.</p>
        </div>
      </footer>
    </div>
  );
};

export default Homepage;

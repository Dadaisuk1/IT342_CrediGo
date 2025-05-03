import React, { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { useAuth } from '../context/AuthContext';

const Shop = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const { refetchWalletAndMails } = useAuth();

  const fetchProducts = async () => {
    try {
      const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
      const res = await fetch(`${API_BASE_URL}/products/getActiveProducts`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      });
      const data = await res.json();
      setProducts(data);
    } catch (error) {
      console.error('Failed to fetch products:', error);
      toast.error('Failed to fetch products. Please try again later.');
    } finally {
      setLoading(false);
    }
  };

  const handleAddToCart = (product) => {
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    cart.push(product);
    localStorage.setItem('cart', JSON.stringify(cart));
    toast.success('Added to cart! 🛒');
    // 🔥 Refresh cart items in Layout without reload
    window.dispatchEvent(new Event('storage'));
  };

  const handleAddWishlist = async (productId) => {
    const token = localStorage.getItem('token');
    const userid = localStorage.getItem('userid');

    if (!userid) {
      toast.error('Please login first.');
      return;
    }

    try {
      const res = await fetch(`${API_BASE_URL}/wishlist/add`, {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          userid: Number(userid),
          productid: Number(productId)
        })
      });

      if (res.ok) {
        toast.success('Added to wishlist! ❤️');
      } else {
        toast.error('Already in wishlist or failed.');
      }
    } catch (error) {
      console.error('Error adding to wishlist:', error);
      toast.error('Something went wrong.');
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-lg font-semibold">Loading products...</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-12 px-4 md:px-8">
      <h1 className="text-3xl font-bold text-center text-[#232946] mb-12">Shop Game Credits</h1>

      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-8">
        {products.map((product) => (
          <div key={product.productid} className="bg-white rounded shadow p-4 flex flex-col justify-between">
            <div>
              <h3 className="text-sm font-medium text-gray-500">{product.category?.categoryname || 'Unknown'}</h3>
              <div className="h-40 bg-gray-200 rounded my-3 flex items-center justify-center">
                <img src={product.imageUrl || 'https://via.placeholder.com/150'} alt={product.productname} className="object-cover h-full w-full rounded" />
              </div>
              <h2 className="font-semibold text-lg text-[#232946]">{product.productname}</h2>
              <div className="text-yellow-400 text-sm mt-1">★★★★☆</div>
            </div>

            <div className="flex justify-between items-center mt-4">
              <div>
                {product.salePrice ? (
                  <div className="flex flex-col">
                    <span className="text-gray-400 line-through text-sm">${product.price}</span>
                    <span className="text-green-600 font-bold">${product.salePrice}</span>
                  </div>
                ) : (
                  <span className="text-green-600 font-bold">${product.price}</span>
                )}
              </div>

              <div className="flex flex-col items-end">
                <button
                  onClick={() => handleAddToCart(product)}
                  className="bg-green-600 hover:bg-green-700 text-white px-3 py-1 rounded text-sm mb-2"
                >
                  Add to Cart
                </button>
                <button
                  onClick={() => handleAddWishlist(product.productid)}
                  className="text-pink-500 hover:text-pink-600 text-xs"
                >
                  ❤️ Add to Wishlist
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Shop;

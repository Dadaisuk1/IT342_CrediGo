import React, { useEffect, useState } from 'react';
import toast from 'react-hot-toast';

const WishList = () => {
  const [wishlist, setWishlist] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchWishlist = async () => {
    const token = localStorage.getItem('token');
    const userid = localStorage.getItem('userid');

    try {
      const res = await fetch(`https://it342-credigo-msd3.onrender.com/api/wishlist/user/${userid}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      const data = await res.json();
      setWishlist(data);
    } catch (error) {
      console.error('Failed to fetch wishlist:', error);
      toast.error('Failed to load wishlist.');
    } finally {
      setLoading(false);
    }
  };

  const removeFromWishlist = async (wishlistId) => {
    const token = localStorage.getItem('token');

    try {
      const res = await fetch(`https://it342-credigo-msd3.onrender.com/api/wishlist/${wishlistId}`, {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      if (res.ok) {
        toast.success('Removed from wishlist.');
        setWishlist((prev) => prev.filter((item) => item.wishlistId !== wishlistId));
      } else {
        toast.error('Failed to remove.');
      }
    } catch (error) {
      console.error('Error removing wishlist:', error);
      toast.error('Something went wrong.');
    }
  };

  useEffect(() => {
    fetchWishlist();
  }, []);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-lg font-semibold">Loading wishlist...</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 px-4 py-10">
      <div className="max-w-4xl mx-auto bg-white shadow-md rounded-lg p-6">
        <h1 className="text-2xl font-bold text-[#232946] mb-6">Your Wishlist</h1>

        {wishlist.length === 0 ? (
          <p className="text-center text-gray-600">Your wishlist is empty. 💔</p>
        ) : (
          <div className="space-y-4">
            {wishlist.map((item) => (
              <div
                key={item.wishlistId}
                className="flex justify-between items-center border p-4 rounded-md shadow-sm"
              >
                <div>
                  <h2 className="font-semibold text-[#232946]">{item.product.productname}</h2>
                  <p className="text-sm text-gray-500">{item.product.category?.categoryname || 'Unknown'}</p>
                  <p className="font-bold text-green-600">${item.product.salePrice || item.product.price}</p>
                </div>
                <div className="flex gap-3">
                  <button
                    onClick={() => removeFromWishlist(item.wishlistId)}
                    className="bg-red-500 hover:bg-red-600 text-white text-sm px-3 py-2 rounded-md"
                  >
                    Remove
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default WishList;

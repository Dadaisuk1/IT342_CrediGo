import React, { useState, useEffect } from 'react';
import { FaSearch, FaEdit, FaTrash, FaPlus } from 'react-icons/fa';
import StatusBadge from '../../components/StatusBadge';
// import adminService from '../api/adminService';

// Mock products data (to be replaced with API call)
const mockProducts = [
  {
    id: 1,
    name: "Basic Credit Card",
    sku: "CC-001",
    category: "Credit Cards",
    description: "Standard credit card with 2% cashback on all purchases",
    interestRate: 18.5,
    annualFee: 0,
    status: "active",
    createdAt: "2025-03-10"
  },
  {
    id: 2,
    name: "Gold Credit Card",
    sku: "CC-002",
    category: "Credit Cards",
    description: "Premium credit card with 3% cashback and travel benefits",
    interestRate: 21.0,
    annualFee: 95,
    status: "active",
    createdAt: "2025-03-12"
  },
  {
    id: 3,
    name: "Personal Loan",
    sku: "LN-001",
    category: "Loans",
    description: "Unsecured personal loan with flexible terms",
    interestRate: 12.5,
    minAmount: 5000,
    maxAmount: 50000,
    status: "active",
    createdAt: "2025-03-15"
  },
  {
    id: 4,
    name: "Student Loan",
    sku: "LN-002",
    category: "Loans",
    description: "Low-interest loans designed for students",
    interestRate: 5.75,
    minAmount: 1000,
    maxAmount: 30000,
    status: "inactive",
    createdAt: "2025-03-20"
  },
  {
    id: 5,
    name: "Premium Checking Account",
    sku: "AC-001",
    category: "Accounts",
    description: "Checking account with no monthly fees and premium benefits",
    interestRate: 0.25,
    monthlyFee: 0,
    status: "active",
    createdAt: "2025-03-25"
  }
];

const ProductManagement = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [categoryFilter, setCategoryFilter] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [page, setPage] = useState(1);
  const [totalProducts, setTotalProducts] = useState(0);
  const [limit, setLimit] = useState(10);
  const [showAddModal, setShowAddModal] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);

  // Product form state
  const [productForm, setProductForm] = useState({
    name: '',
    sku: '',
    category: '',
    description: '',
    interestRate: 0,
    annualFee: 0,
    status: 'active'
  });

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        setLoading(true);
        // In a real implementation, this would be an API call
        // const data = await adminService.getProducts(page, limit, searchTerm, categoryFilter, statusFilter);

        // Mock implementation
        setTimeout(() => {
          const filteredProducts = mockProducts.filter(product => {
            const matchesSearch = searchTerm === '' ||
              product.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
              product.sku.toLowerCase().includes(searchTerm.toLowerCase());

            const matchesCategory = categoryFilter === '' || product.category === categoryFilter;
            const matchesStatus = statusFilter === '' || product.status === statusFilter;

            return matchesSearch && matchesCategory && matchesStatus;
          });

          setProducts(filteredProducts);
          setTotalProducts(filteredProducts.length);
          setLoading(false);
          setError(null);
        }, 500);
      } catch (err) {
        console.error('Error fetching products:', err);
        setError('Failed to load product data');
        setLoading(false);
      }
    };

    fetchProducts();
  }, [page, limit, searchTerm, categoryFilter, statusFilter]);

  const handleSearch = (e) => {
    e.preventDefault();
    setPage(1); // Reset to first page when searching
  };

  const handlePrevPage = () => {
    if (page > 1) {
      setPage(page - 1);
    }
  };

  const handleNextPage = () => {
    if (page * limit < totalProducts) {
      setPage(page + 1);
    }
  };

  const handleAddProduct = () => {
    setEditingProduct(null);
    setProductForm({
      name: '',
      sku: '',
      category: '',
      description: '',
      interestRate: 0,
      annualFee: 0,
      status: 'active'
    });
    setShowAddModal(true);
  };

  const handleEditProduct = (product) => {
    setEditingProduct(product);
    setProductForm({
      name: product.name,
      sku: product.sku,
      category: product.category,
      description: product.description,
      interestRate: product.interestRate,
      annualFee: product.annualFee || 0,
      status: product.status
    });
    setShowAddModal(true);
  };

  const handleDeleteProduct = async (productId) => {
    if (window.confirm('Are you sure you want to delete this product?')) {
      try {
        // In a real implementation, this would be an API call
        // await adminService.deleteProduct(productId);

        // Mock implementation
        const updatedProducts = products.filter(product => product.id !== productId);
        setProducts(updatedProducts);
        setTotalProducts(updatedProducts.length);
        alert('Product deleted successfully');
      } catch (err) {
        console.error('Error deleting product:', err);
        alert('Failed to delete product');
      }
    }
  };

  const handleFormChange = (e) => {
    const { name, value } = e.target;
    setProductForm(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmitProduct = async (e) => {
    e.preventDefault();

    try {
      if (editingProduct) {
        // Update existing product
        // In a real implementation, this would be an API call
        // const updatedProduct = await adminService.updateProduct(editingProduct.id, productForm);

        // Mock implementation
        const updatedProducts = products.map(product =>
          product.id === editingProduct.id
            ? { ...product, ...productForm }
            : product
        );
        setProducts(updatedProducts);
        alert('Product updated successfully');
      } else {
        // Create new product
        // In a real implementation, this would be an API call
        // const newProduct = await adminService.createProduct(productForm);

        // Mock implementation
        const newProduct = {
          id: Date.now(),
          ...productForm,
          createdAt: new Date().toISOString().split('T')[0]
        };
        setProducts([...products, newProduct]);
        setTotalProducts(totalProducts + 1);
        alert('Product created successfully');
      }

      setShowAddModal(false);
    } catch (err) {
      console.error('Error saving product:', err);
      alert('Failed to save product');
    }
  };

  // Unique categories from products data
  const categories = [...new Set(mockProducts.map(product => product.category))];

  return (
    <div className="bg-white rounded-lg shadow">
      <div className="p-6 border-b border-gray-200 flex justify-between items-center">
        <h2 className="text-lg font-semibold text-[#232946]">Product Management</h2>
        <button
          onClick={handleAddProduct}
          className="bg-[#6285cf] text-white px-4 py-2 rounded-md hover:bg-[#445ab1] transition-colors duration-200 flex items-center"
        >
          <FaPlus className="mr-2" /> Add New Product
        </button>
      </div>

      <div className="p-6">
        <div className="flex flex-col md:flex-row justify-between mb-4">
          <form onSubmit={handleSearch} className="relative mb-4 md:mb-0">
            <input
              type="text"
              placeholder="Search products..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-10 pr-4 py-2 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-[#6285cf] focus:border-transparent"
            />
            <FaSearch className="absolute left-3 top-3 text-gray-400" />
            <button type="submit" className="hidden">Search</button>
          </form>

          <div className="flex space-x-2">
            <select
              className="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#6285cf] focus:border-transparent"
              value={categoryFilter}
              onChange={(e) => setCategoryFilter(e.target.value)}
            >
              <option value="">All Categories</option>
              {categories.map(category => (
                <option key={category} value={category}>{category}</option>
              ))}
            </select>
            <select
              className="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#6285cf] focus:border-transparent"
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
            >
              <option value="">All Status</option>
              <option value="active">Active</option>
              <option value="inactive">Inactive</option>
            </select>
          </div>
        </div>

        {loading ? (
          <div className="flex justify-center items-center h-64">Loading product data...</div>
        ) : error ? (
          <div className="bg-red-100 p-4 rounded-lg text-red-700">{error}</div>
        ) : (
          <>
            <div className="overflow-x-auto mt-4">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Product
                    </th>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Category
                    </th>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Interest Rate
                    </th>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Status
                    </th>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Created On
                    </th>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Actions
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {products.map((product) => (
                    <tr key={product.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="flex items-center">
                          <div className="ml-4">
                            <div className="text-sm font-medium text-gray-900">{product.name}</div>
                            <div className="text-sm text-gray-500">SKU: {product.sku}</div>
                          </div>
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="text-sm text-gray-900">{product.category}</div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="text-sm text-gray-900">{product.interestRate}%</div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <StatusBadge status={product.status} />
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {product.createdAt}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        <button
                          onClick={() => handleEditProduct(product)}
                          className="text-[#6285cf] hover:text-[#445ab1] mr-3"
                        >
                          <FaEdit className="inline mr-1" /> Edit
                        </button>
                        <button
                          onClick={() => handleDeleteProduct(product.id)}
                          className="text-red-600 hover:text-red-900"
                        >
                          <FaTrash className="inline mr-1" /> Delete
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            <div className="mt-4 flex items-center justify-between">
              <div className="text-sm text-gray-700">
                Showing <span className="font-medium">{products.length === 0 ? 0 : (page - 1) * limit + 1}</span> to{' '}
                <span className="font-medium">{Math.min(page * limit, totalProducts)}</span> of{' '}
                <span className="font-medium">{totalProducts}</span> results
              </div>
              <div className="flex space-x-2">
                <button
                  onClick={handlePrevPage}
                  disabled={page === 1}
                  className={`${page === 1 ? 'bg-gray-100 text-gray-400 cursor-not-allowed' : 'bg-white text-gray-500 hover:bg-gray-50'} border border-gray-300 px-4 py-2 rounded-md`}
                >
                  Previous
                </button>
                <button
                  onClick={handleNextPage}
                  disabled={page * limit >= totalProducts}
                  className={`${page * limit >= totalProducts ? 'bg-gray-100 text-gray-400 cursor-not-allowed' : 'bg-[#6285cf] text-white hover:bg-[#445ab1]'} px-4 py-2 rounded-md`}
                >
                  Next
                </button>
              </div>
            </div>
          </>
        )}
      </div>

      {/* Add/Edit Product Modal */}
      {showAddModal && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50 flex items-center justify-center">
          <div className="relative bg-white rounded-lg shadow-xl max-w-2xl w-full mx-4 md:mx-auto">
            <div className="flex justify-between items-center p-6 border-b border-gray-200">
              <h3 className="text-lg font-semibold text-[#232946]">
                {editingProduct ? 'Edit Product' : 'Add New Product'}
              </h3>
              <button
                onClick={() => setShowAddModal(false)}
                className="text-gray-400 hover:text-gray-500 focus:outline-none"
              >
                &times;
              </button>
            </div>

            <form onSubmit={handleSubmitProduct} className="p-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Product Name</label>
                  <input
                    type="text"
                    name="name"
                    value={productForm.name}
                    onChange={handleFormChange}
                    required
                    className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#6285cf] focus:border-transparent"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">SKU</label>
                  <input
                    type="text"
                    name="sku"
                    value={productForm.sku}
                    onChange={handleFormChange}
                    required
                    className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#6285cf] focus:border-transparent"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Category</label>
                  <select
                    name="category"
                    value={productForm.category}
                    onChange={handleFormChange}
                    required
                    className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#6285cf] focus:border-transparent"
                  >
                    <option value="">Select Category</option>
                    {categories.map(category => (
                      <option key={category} value={category}>{category}</option>
                    ))}
                    <option value="Other">Other</option>
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Status</label>
                  <select
                    name="status"
                    value={productForm.status}
                    onChange={handleFormChange}
                    className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#6285cf] focus:border-transparent"
                  >
                    <option value="active">Active</option>
                    <option value="inactive">Inactive</option>
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Interest Rate (%)</label>
                  <input
                    type="number"
                    name="interestRate"
                    value={productForm.interestRate}
                    onChange={handleFormChange}
                    min="0"
                    step="0.01"
                    className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#6285cf] focus:border-transparent"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Annual Fee ($)</label>
                  <input
                    type="number"
                    name="annualFee"
                    value={productForm.annualFee}
                    onChange={handleFormChange}
                    min="0"
                    className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#6285cf] focus:border-transparent"
                  />
                </div>

                <div className="md:col-span-2">
                  <label className="block text-sm font-medium text-gray-700 mb-1">Description</label>
                  <textarea
                    name="description"
                    value={productForm.description}
                    onChange={handleFormChange}
                    rows="3"
                    className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#6285cf] focus:border-transparent"
                  ></textarea>
                </div>
              </div>

              <div className="mt-6 flex justify-end space-x-3">
                <button
                  type="button"
                  onClick={() => setShowAddModal(false)}
                  className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-4 py-2 bg-[#6285cf] text-white rounded-md hover:bg-[#445ab1]"
                >
                  {editingProduct ? 'Update Product' : 'Add Product'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default ProductManagement;

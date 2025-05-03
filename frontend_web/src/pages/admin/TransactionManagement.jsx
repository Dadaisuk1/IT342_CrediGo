import React, { useState, useEffect } from 'react';
import { FaSearch, FaFileExport, FaFilter, FaEye, FaPrint } from 'react-icons/fa';

// Mock data for demonstration
const mockTransactions = [
  { id: 1, user: "John Doe", amount: 250.00, date: "2025-04-15", status: "completed", type: "purchase", product: "Premium Plan" },
  { id: 2, user: "Jane Smith", amount: 120.50, date: "2025-04-14", status: "pending", type: "subscription", product: "Basic Plan" },
  { id: 3, user: "Bob Johnson", amount: 75.25, date: "2025-04-13", status: "completed", type: "purchase", product: "Standard Plan" },
  { id: 4, user: "Alice Brown", amount: 340.00, date: "2025-04-12", status: "completed", type: "renewal", product: "Premium Plan" },
  { id: 5, user: "Charlie Wilson", amount: 95.75, date: "2025-04-11", status: "declined", type: "purchase", product: "Basic Plan" },
  { id: 6, user: "Eva Garcia", amount: 180.25, date: "2025-04-10", status: "completed", type: "subscription", product: "Standard Plan" },
  { id: 7, user: "David Lee", amount: 210.00, date: "2025-04-09", status: "pending", type: "purchase", product: "Premium Plan" },
  { id: 8, user: "Grace Taylor", amount: 65.50, date: "2025-04-08", status: "completed", type: "renewal", product: "Basic Plan" },
  { id: 9, user: "Frank Rodriguez", amount: 425.75, date: "2025-04-07", status: "declined", type: "purchase", product: "Premium Plus Plan" },
  { id: 10, user: "Helen Martinez", amount: 150.00, date: "2025-04-06", status: "completed", type: "subscription", product: "Standard Plan" },
];

const TransactionManagement = () => {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState('all');
  const [typeFilter, setTypeFilter] = useState('all');
  const [dateRange, setDateRange] = useState({ start: '', end: '' });
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(5);
  const [selectedTransaction, setSelectedTransaction] = useState(null);
  const [showTransactionDetails, setShowTransactionDetails] = useState(false);

  // Fetch transaction data
  useEffect(() => {
    // Simulate API call
    const fetchTransactions = async () => {
      try {
        // In a real application, this would be an API call
        // const response = await fetch('https://it342-credigo-msd3.onrender.com/api/transactions', {
        //   headers: {
        //     'Authorization': `Bearer ${localStorage.getItem('token')}`
        //   }
        // });
        // const data = await response.json();

        // Using mock data instead
        setTimeout(() => {
          setTransactions(mockTransactions);
          setLoading(false);
        }, 500);
      } catch (error) {
        console.error('Error fetching transactions:', error);
        setLoading(false);
      }
    };

    fetchTransactions();
  }, []);

  // Filter transactions based on search and filters
  const filteredTransactions = transactions.filter(transaction => {
    // Search filter
    const matchesSearch = searchQuery === '' ||
      transaction.user.toLowerCase().includes(searchQuery.toLowerCase()) ||
      transaction.id.toString().includes(searchQuery) ||
      transaction.product.toLowerCase().includes(searchQuery.toLowerCase());

    // Status filter
    const matchesStatus = statusFilter === 'all' || transaction.status === statusFilter;

    // Type filter
    const matchesType = typeFilter === 'all' || transaction.type === typeFilter;

    // Date range filter
    const transactionDate = new Date(transaction.date);
    const matchesDateRange =
      (dateRange.start === '' || new Date(dateRange.start) <= transactionDate) &&
      (dateRange.end === '' || new Date(dateRange.end) >= transactionDate);

    return matchesSearch && matchesStatus && matchesType && matchesDateRange;
  });

  // Pagination
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentTransactions = filteredTransactions.slice(indexOfFirstItem, indexOfLastItem);
  const totalPages = Math.ceil(filteredTransactions.length / itemsPerPage);

  // Status badge styling function
  const getStatusBadgeClass = (status) => {
    switch(status) {
      case 'completed':
        return 'bg-green-100 text-green-800';
      case 'pending':
        return 'bg-yellow-100 text-yellow-800';
      case 'declined':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  // Type badge styling function
  const getTypeBadgeClass = (type) => {
    switch(type) {
      case 'purchase':
        return 'bg-blue-100 text-blue-800';
      case 'subscription':
        return 'bg-purple-100 text-purple-800';
      case 'renewal':
        return 'bg-indigo-100 text-indigo-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  // View transaction details
  const viewTransactionDetails = (transaction) => {
    setSelectedTransaction(transaction);
    setShowTransactionDetails(true);
  };

  // Reset filters
  const resetFilters = () => {
    setSearchQuery('');
    setStatusFilter('all');
    setTypeFilter('all');
    setDateRange({ start: '', end: '' });
    setCurrentPage(1);
  };

  // Export transactions (placeholder function)
  const exportTransactions = () => {
    alert('Export functionality would be implemented here');
    // In a real application, this would generate a CSV or PDF file
  };

  // Print transaction (placeholder function)
  const printTransaction = (id) => {
    alert(`Print transaction #${id}`);
    // In a real application, this would open a print dialog with formatted transaction details
  };

  return (
    <div className="space-y-6">
      {/* Title and Export button */}
      <div className="flex justify-between items-center">
        <h2 className="text-xl font-semibold text-[#232946]">Transaction Management</h2>
        <button
          onClick={exportTransactions}
          className="flex items-center bg-[#6285cf] text-white px-4 py-2 rounded-md hover:bg-[#445ab1] transition-colors duration-200"
        >
          <FaFileExport className="mr-2" />
          Export
        </button>
      </div>

      {/* Filters Section */}
      <div className="bg-white rounded-lg shadow p-6">
        <div className="flex flex-col md:flex-row items-center justify-between mb-4">
          <h3 className="text-lg font-medium text-[#232946] mb-4 md:mb-0">Filters</h3>
          <button
            onClick={resetFilters}
            className="text-[#6285cf] hover:text-[#445ab1] font-medium"
          >
            Reset Filters
          </button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          {/* Search */}
          <div className="relative">
            <input
              type="text"
              placeholder="Search transactions..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="pl-10 pr-4 py-2 w-full rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-[#6285cf] focus:border-transparent"
            />
            <FaSearch className="absolute left-3 top-3 text-gray-400" />
          </div>

          {/* Status Filter */}
          <div>
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#6285cf] focus:border-transparent"
            >
              <option value="all">All Statuses</option>
              <option value="completed">Completed</option>
              <option value="pending">Pending</option>
              <option value="declined">Declined</option>
            </select>
          </div>

          {/* Type Filter */}
          <div>
            <select
              value={typeFilter}
              onChange={(e) => setTypeFilter(e.target.value)}
              className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#6285cf] focus:border-transparent"
            >
              <option value="all">All Types</option>
              <option value="purchase">Purchase</option>
              <option value="subscription">Subscription</option>
              <option value="renewal">Renewal</option>
            </select>
          </div>

          {/* Date Range Filter - This would be better with a date picker library */}
          <div className="flex space-x-2">
            <input
              type="date"
              value={dateRange.start}
              onChange={(e) => setDateRange({...dateRange, start: e.target.value})}
              className="w-1/2 border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#6285cf] focus:border-transparent"
            />
            <input
              type="date"
              value={dateRange.end}
              onChange={(e) => setDateRange({...dateRange, end: e.target.value})}
              className="w-1/2 border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#6285cf] focus:border-transparent"
            />
          </div>
        </div>
      </div>

      {/* Transactions Table */}
      <div className="bg-white rounded-lg shadow">
        <div className="p-6 border-b border-gray-200">
          <h3 className="text-lg font-semibold text-[#232946]">Transactions List</h3>
        </div>

        {loading ? (
          <div className="p-6 text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-[#6285cf] mx-auto"></div>
            <p className="mt-4 text-gray-500">Loading transactions...</p>
          </div>
        ) : (
          <>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">User</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Amount</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Product</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Type</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {currentTransactions.length > 0 ? (
                    currentTransactions.map((transaction) => (
                      <tr key={transaction.id} className="hover:bg-gray-50">
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">#{transaction.id}</td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{transaction.user}</td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${transaction.amount.toFixed(2)}</td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{transaction.date}</td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{transaction.product}</td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${getTypeBadgeClass(transaction.type)}`}>
                            {transaction.type.charAt(0).toUpperCase() + transaction.type.slice(1)}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${getStatusBadgeClass(transaction.status)}`}>
                            {transaction.status.charAt(0).toUpperCase() + transaction.status.slice(1)}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                          <button
                            onClick={() => viewTransactionDetails(transaction)}
                            className="text-[#6285cf] hover:text-[#445ab1] mr-3"
                            title="View Details"
                          >
                            <FaEye />
                          </button>
                          <button
                            onClick={() => printTransaction(transaction.id)}
                            className="text-gray-600 hover:text-gray-900"
                            title="Print Receipt"
                          >
                            <FaPrint />
                          </button>
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="8" className="px-6 py-4 text-center text-gray-500">
                        No transactions found matching your criteria
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>

            {/* Pagination */}
            <div className="px-6 py-4 border-t border-gray-200 flex items-center justify-between">
              <div className="text-sm text-gray-700">
                Showing <span className="font-medium">{indexOfFirstItem + 1}</span> to{' '}
                <span className="font-medium">
                  {indexOfLastItem > filteredTransactions.length ? filteredTransactions.length : indexOfLastItem}
                </span>{' '}
                of <span className="font-medium">{filteredTransactions.length}</span> results
              </div>
              <div className="flex space-x-2">
                <button
                  onClick={() => setCurrentPage(currentPage > 1 ? currentPage - 1 : 1)}
                  disabled={currentPage === 1}
                  className={`${
                    currentPage === 1
                      ? 'bg-gray-100 text-gray-400 cursor-not-allowed'
                      : 'bg-white text-gray-500 hover:bg-gray-50'
                  } border border-gray-300 px-4 py-2 rounded-md`}
                >
                  Previous
                </button>
                <button
                  onClick={() => setCurrentPage(currentPage < totalPages ? currentPage + 1 : totalPages)}
                  disabled={currentPage === totalPages || totalPages === 0}
                  className={`${
                    currentPage === totalPages || totalPages === 0
                      ? 'bg-gray-100 text-gray-400 cursor-not-allowed'
                      : 'bg-[#6285cf] text-white hover:bg-[#445ab1]'
                  } px-4 py-2 rounded-md`}
                >
                  Next
                </button>
              </div>
            </div>
          </>
        )}
      </div>

      {/* Transaction Details Modal */}
      {showTransactionDetails && selectedTransaction && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-lg max-w-md w-full">
            <div className="p-6 border-b border-gray-200 flex justify-between items-center">
              <h3 className="text-lg font-semibold text-[#232946]">Transaction Details</h3>
              <button
                onClick={() => setShowTransactionDetails(false)}
                className="text-gray-500 hover:text-gray-700 focus:outline-none"
              >
                <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>

            <div className="p-6">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <p className="text-sm font-medium text-gray-500">Transaction ID</p>
                  <p className="text-base font-semibold">#{selectedTransaction.id}</p>
                </div>
                <div>
                  <p className="text-sm font-medium text-gray-500">Date</p>
                  <p className="text-base">{selectedTransaction.date}</p>
                </div>
                <div>
                  <p className="text-sm font-medium text-gray-500">User</p>
                  <p className="text-base">{selectedTransaction.user}</p>
                </div>
                <div>
                  <p className="text-sm font-medium text-gray-500">Amount</p>
                  <p className="text-base font-semibold">${selectedTransaction.amount.toFixed(2)}</p>
                </div>
                <div>
                  <p className="text-sm font-medium text-gray-500">Product</p>
                  <p className="text-base">{selectedTransaction.product}</p>
                </div>
                <div>
                  <p className="text-sm font-medium text-gray-500">Type</p>
                  <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${getTypeBadgeClass(selectedTransaction.type)}`}>
                    {selectedTransaction.type.charAt(0).toUpperCase() + selectedTransaction.type.slice(1)}
                  </span>
                </div>
                <div>
                  <p className="text-sm font-medium text-gray-500">Status</p>
                  <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${getStatusBadgeClass(selectedTransaction.status)}`}>
                    {selectedTransaction.status.charAt(0).toUpperCase() + selectedTransaction.status.slice(1)}
                  </span>
                </div>
              </div>

              <div className="mt-6 flex justify-between">
                <button
                  onClick={() => printTransaction(selectedTransaction.id)}
                  className="flex items-center bg-gray-100 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-200 transition-colors duration-200"
                >
                  <FaPrint className="mr-2" />
                  Print Receipt
                </button>

                {selectedTransaction.status === 'pending' && (
                  <div className="space-x-2">
                    <button className="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 transition-colors duration-200">
                      Approve
                    </button>
                    <button className="bg-red-500 text-white px-4 py-2 rounded-md hover:bg-red-600 transition-colors duration-200">
                      Decline
                    </button>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default TransactionManagement;

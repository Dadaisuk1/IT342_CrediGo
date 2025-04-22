import React from 'react';

const StatCard = ({ title, value, icon, change, iconBgColor, iconColor }) => {
  return (
    <div className="bg-white rounded-lg shadow p-6">
      <div className="flex justify-between items-center">
        <div>
          <p className="text-sm font-medium text-gray-500">{title}</p>
          <p className="text-2xl font-semibold text-[#232946]">{value}</p>
        </div>
        <div className={`p-3 ${iconBgColor} rounded-full`}>
          {icon}
        </div>
      </div>
      <p className="mt-2 text-sm text-gray-500">
        <span className={change >= 0 ? "text-green-500" : "text-red-500"}>
          {change >= 0 ? '+' : ''}{change}%
        </span> from last month
      </p>
    </div>
  );
};

export default StatCard;

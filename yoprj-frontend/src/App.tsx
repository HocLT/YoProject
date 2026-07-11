import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClientProvider } from '@tanstack/react-query';
import { queryClient } from './lib/react-query';
import { AuthProvider } from './context/AuthProvider';
import { ProtectedRoute, RoleGuard } from './routes/guards';
import { DashboardLayout } from './components/layout/DashboardLayout';
import { LoginPage } from './features/auth/components/LoginPage';
import { UserRole } from './config/constants';

// Placeholder components for routes
const Dashboard = () => <div className="p-6 bg-white rounded-lg shadow"><h2 className="text-2xl font-bold mb-4">Dashboard</h2><p>Welcome to YOEDU system.</p></div>;
const Students = () => <div className="p-6 bg-white rounded-lg shadow"><h2 className="text-2xl font-bold mb-4">Students Management</h2></div>;
const Teachers = () => <div className="p-6 bg-white rounded-lg shadow"><h2 className="text-2xl font-bold mb-4">Teachers Management</h2></div>;
const Courses = () => <div className="p-6 bg-white rounded-lg shadow"><h2 className="text-2xl font-bold mb-4">Courses Management</h2></div>;
const Classes = () => <div className="p-6 bg-white rounded-lg shadow"><h2 className="text-2xl font-bold mb-4">Classes Management</h2></div>;
const Enrollments = () => <div className="p-6 bg-white rounded-lg shadow"><h2 className="text-2xl font-bold mb-4">Enrollments Management</h2></div>;
const Attendance = () => <div className="p-6 bg-white rounded-lg shadow"><h2 className="text-2xl font-bold mb-4">Attendance Tracking</h2></div>;
const Billing = () => <div className="p-6 bg-white rounded-lg shadow"><h2 className="text-2xl font-bold mb-4">Billing System</h2></div>;
const Payments = () => <div className="p-6 bg-white rounded-lg shadow"><h2 className="text-2xl font-bold mb-4">Payments Management</h2></div>;
const Promotions = () => <div className="p-6 bg-white rounded-lg shadow"><h2 className="text-2xl font-bold mb-4">Promotions & Vouchers</h2></div>;
const Rooms = () => <div className="p-6 bg-white rounded-lg shadow"><h2 className="text-2xl font-bold mb-4">Rooms Management</h2></div>;
const ScheduleSlots = () => <div className="p-6 bg-white rounded-lg shadow"><h2 className="text-2xl font-bold mb-4">Schedule Slots Management</h2></div>;
const Reports = () => <div className="p-6 bg-white rounded-lg shadow"><h2 className="text-2xl font-bold mb-4">System Reports</h2></div>;
const ParentDashboard = () => <div className="p-6 bg-white rounded-lg shadow"><h2 className="text-2xl font-bold mb-4">Parent Portal</h2><p>View your children's progress here.</p></div>;
const Unauthorized = () => <div className="p-6 bg-white rounded-lg shadow text-red-600"><h2 className="text-2xl font-bold mb-4">Unauthorized Access</h2><p>You do not have permission to view this page.</p></div>;

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            
            <Route element={<ProtectedRoute />}>
              <Route element={<DashboardLayout />}>
                
                {/* Admin, Academic Staff & Cashier Routes */}
                <Route element={<RoleGuard allowedRoles={[UserRole.ADMIN, UserRole.ACADEMIC_STAFF, UserRole.CASHIER]} />}>
                  <Route path="/dashboard" element={<Dashboard />} />
                  <Route path="/students" element={<Students />} />
                  <Route path="/classes" element={<Classes />} />
                </Route>

                {/* Admin & Academic Staff Routes */}
                <Route element={<RoleGuard allowedRoles={[UserRole.ADMIN, UserRole.ACADEMIC_STAFF]} />}>
                  <Route path="/teachers" element={<Teachers />} />
                  <Route path="/courses" element={<Courses />} />
                  <Route path="/enrollments" element={<Enrollments />} />
                  <Route path="/attendance" element={<Attendance />} />
                  <Route path="/rooms" element={<Rooms />} />
                  <Route path="/schedule-slots" element={<ScheduleSlots />} />
                </Route>

                {/* Admin & Cashier Routes */}
                <Route element={<RoleGuard allowedRoles={[UserRole.ADMIN, UserRole.CASHIER]} />}>
                  <Route path="/promotions" element={<Promotions />} />
                  <Route path="/billing" element={<Billing />} />
                  <Route path="/payments" element={<Payments />} />
                  <Route path="/reports" element={<Reports />} />
                </Route>

                {/* Parent Portal Route */}
                <Route element={<RoleGuard allowedRoles={[UserRole.PARENT]} />}>
                  <Route path="/parent" element={<ParentDashboard />} />
                </Route>

                <Route path="/unauthorized" element={<Unauthorized />} />
                
                {/* Default redirect to dashboard */}
                <Route path="/" element={<Navigate to="/dashboard" replace />} />
              </Route>
            </Route>

            {/* Fallback */}
            <Route path="*" element={<Navigate to="/login" replace />} />
          </Routes>
        </BrowserRouter>
      </AuthProvider>
    </QueryClientProvider>
  );
}

export default App;

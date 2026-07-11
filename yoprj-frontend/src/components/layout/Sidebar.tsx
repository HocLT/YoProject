import React from 'react';
import { NavLink } from 'react-router-dom';
import { useAuth } from '../../context/AuthProvider';
import { 
  LayoutDashboard, Users, BookOpen, Presentation, UserCheck, 
  CreditCard, Calendar, DoorOpen, BadgePercent, GraduationCap, ClipboardList 
} from 'lucide-react';
import { UserRole } from '../../config/constants';

interface NavItem {
  label: string;
  path: string;
  icon: React.ElementType;
  roles: UserRole[];
}

const navItems: NavItem[] = [
  { label: 'Dashboard', path: '/dashboard', icon: LayoutDashboard, roles: [UserRole.ADMIN, UserRole.ACADEMIC_STAFF, UserRole.CASHIER] },
  { label: 'Parent Dashboard', path: '/parent', icon: LayoutDashboard, roles: [UserRole.PARENT] },
  { label: 'Users', path: '/users', icon: Users, roles: [UserRole.ADMIN] },
  { label: 'Teachers', path: '/teachers', icon: Presentation, roles: [UserRole.ADMIN, UserRole.ACADEMIC_STAFF] },
  { label: 'Students', path: '/students', icon: GraduationCap, roles: [UserRole.ADMIN, UserRole.ACADEMIC_STAFF, UserRole.CASHIER] },
  { label: 'Courses', path: '/courses', icon: BookOpen, roles: [UserRole.ADMIN, UserRole.ACADEMIC_STAFF] },
  { label: 'Classes', path: '/classes', icon: ClipboardList, roles: [UserRole.ADMIN, UserRole.ACADEMIC_STAFF, UserRole.CASHIER] },
  { label: 'Enrollments', path: '/enrollments', icon: UserCheck, roles: [UserRole.ADMIN, UserRole.ACADEMIC_STAFF] },
  { label: 'Attendance', path: '/attendance', icon: UserCheck, roles: [UserRole.ADMIN, UserRole.ACADEMIC_STAFF] },
  { label: 'Rooms', path: '/rooms', icon: DoorOpen, roles: [UserRole.ADMIN, UserRole.ACADEMIC_STAFF] },
  { label: 'Schedule Slots', path: '/schedule-slots', icon: Calendar, roles: [UserRole.ADMIN, UserRole.ACADEMIC_STAFF] },
  { label: 'Promotions', path: '/promotions', icon: BadgePercent, roles: [UserRole.ADMIN, UserRole.CASHIER] },
];

export const Sidebar = () => {
  const { user } = useAuth();

  const filteredNavItems = navItems.filter((item) => user && item.roles.includes(user.role));

  return (
    <div className="w-64 bg-gray-900 text-white flex flex-col h-screen">
      <div className="p-4 flex items-center justify-center border-b border-gray-800">
        <h1 className="text-2xl font-bold tracking-wider">YOEDU</h1>
      </div>
      <nav className="flex-1 p-4 space-y-2 overflow-y-auto">
        {filteredNavItems.map((item) => {
          const Icon = item.icon;
          return (
            <NavLink
              key={item.path}
              to={item.path}
              className={({ isActive }) =>
                `flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                  isActive ? 'bg-blue-600 text-white' : 'text-gray-300 hover:bg-gray-800 hover:text-white'
                }`
              }
            >
              <Icon className="w-5 h-5" />
              <span className="font-medium">{item.label}</span>
            </NavLink>
          );
        })}
      </nav>
    </div>
  );
};

import {
  UserRole,
  Gender,
  ClassStatus,
  EnrollmentStatus,
  AttendanceStatus,
  DiscountType,
  TeacherRole
} from '../config/constants';

export interface PageResponse<T> {
  content: T[];
  pageable: any;
  totalPages: number;
  totalElements: number;
  last: boolean;
  size: number;
  number: number;
  sort: any;
  numberOfElements: number;
  first: boolean;
  empty: boolean;
}

export interface CurrentUserResponse {
  id: number;
  username: string;
  fullName: string;
  role: UserRole;
  parentId?: number;
  teacherId?: number;
}

export interface AuthResponse {
  accessToken: string;
  tokenType: string;
  expiresAt: string;
  user: CurrentUserResponse;
}

export interface UserResponse {
  id: number;
  username: string;
  fullName: string;
  phone?: string;
  email?: string;
  role: UserRole;
  parentId?: number;
  teacherId?: number;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface TeacherResponse {
  id: number;
  teacherCode: string;
  fullName: string;
  phone: string;
  email?: string;
  teacherRole: TeacherRole;
  cccdImageUrl?: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface StudentResponse {
  id: number;
  studentCode: string;
  fullName: string;
  dateOfBirth?: string;
  gender: Gender;
  gradeLevel: string;
  schoolName?: string;
  phone?: string;
  parentId: number;
  parentName?: string;
  status: string;
  latestScore?: number;
  note?: string;
  createdAt: string;
  updatedAt: string;
}

export interface ParentResponse {
  id: number;
  fullName: string;
  phone: string;
  email?: string;
  address?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CourseResponse {
  id: number;
  courseCode: string;
  name: string;
  description?: string;
  tuitionFee: number;
  totalSessions: number;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface CourseClassResponse {
  id: number;
  classCode: string;
  name: string;
  courseId: number;
  courseName: string;
  roomId: number;
  roomName: string;
  scheduleSlotId: number;
  scheduleLabel: string;
  mainTeacherId: number;
  mainTeacherName: string;
  assistantTeacherId?: number;
  assistantTeacherName?: string;
  startDate: string;
  endDate?: string;
  maxStudents: number;
  tuitionFee: number;
  status: ClassStatus;
  createdAt: string;
  updatedAt: string;
}

export interface RoomResponse {
  id: number;
  roomCode: string;
  name: string;
  capacity: number;
  description?: string;
  createdAt: string;
  updatedAt: string;
}

export interface ScheduleSlotResponse {
  id: number;
  slotCode: string;
  weekday: number;
  startTime: string;
  endTime: string;
  note?: string;
  createdAt: string;
  updatedAt: string;
}

export interface EnrollmentResponse {
  id: number;
  studentId: number;
  studentName: string;
  courseClassId: number;
  className: string;
  enrolledAt: string;
  status: EnrollmentStatus;
  note?: string;
  createdAt: string;
  updatedAt: string;
}

export interface AttendanceResponse {
  id: number;
  courseClassId: number;
  className: string;
  studentId: number;
  studentName: string;
  attendanceDate: string;
  status: AttendanceStatus;
  note?: string;
  recordedByUserId?: number;
  recordedByUsername?: string;
  createdAt: string;
  updatedAt: string;
}

export interface LearningResultResponse {
  id: number;
  studentId: number;
  studentName: string;
  courseClassId: number;
  className: string;
  resultMonth: string;
  score: number;
  teacherComment?: string;
  createdByUserId?: number;
  createdByUsername?: string;
  createdAt: string;
  updatedAt: string;
}

export interface PromotionResponse {
  id: number;
  promoCode: string;
  name: string;
  discountType: DiscountType;
  discountValue: number;
  startDate: string;
  endDate: string;
  isActive: boolean;
  note?: string;
  createdAt: string;
  updatedAt: string;
}

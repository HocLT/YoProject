export enum UserRole {
  ADMIN = 'ADMIN',
  ACADEMIC_STAFF = 'ACADEMIC_STAFF',
  CASHIER = 'CASHIER',
  PARENT = 'PARENT'
}

export enum Gender {
  MALE = 'MALE',
  FEMALE = 'FEMALE',
  OTHER = 'OTHER'
}

export enum Status {
  ACTIVE = 'ACTIVE',
  PAUSED = 'PAUSED',
  DROPPED = 'DROPPED'
}

export enum ClassStatus {
  OPEN = 'OPEN',
  ONGOING = 'ONGOING',
  CLOSED = 'CLOSED',
  FULL = 'FULL'
}

export enum EnrollmentStatus {
  ACTIVE = 'ACTIVE',
  PAUSED = 'PAUSED',
  DROPPED = 'DROPPED',
  COMPLETED = 'COMPLETED'
}

export enum AttendanceStatus {
  PRESENT = 'PRESENT',
  ABSENT = 'ABSENT',
  LATE = 'LATE',
  EXCUSED = 'EXCUSED'
}

export enum DiscountType {
  PERCENT = 'PERCENT',
  AMOUNT = 'AMOUNT'
}

export enum TeacherRole {
  TEACHER = 'TEACHER',
  ASSISTANT = 'ASSISTANT',
  BOTH = 'BOTH'
}

import { apiClient } from '../../lib/api';
import { AuthResponse, CurrentUserResponse } from '../../types/api';

export interface LoginRequest {
  username: string;
  passwordHash: string;
}

export const authApi = {
  login: async (data: LoginRequest): Promise<AuthResponse> => {
    return apiClient.post('/api/auth/login', data);
  },
  me: async (): Promise<CurrentUserResponse> => {
    return apiClient.get('/api/auth/me');
  },
};

import { apiClient } from "./apiClient";

export type UserResponse = {
  id: string;
  name: string;
  email: string;
  country: string;
  provider: string;
};

export type ApiResponse<T> = {
  timestamp: string;
  success: boolean;
  message: string;
  data: T;
};

export async function getCurrentUser(): Promise<UserResponse> {
  const response = await apiClient.get<ApiResponse<UserResponse>>("/api/users/me");
  return response.data.data;
}
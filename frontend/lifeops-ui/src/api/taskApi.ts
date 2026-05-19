import { apiClient } from "./apiClient";

export type TaskResponse = {
  id: string;
  userId: string | null;
  sourceType: string;
  sourceId: string | null;
  title: string;
  description: string | null;
  recommendedAction: string | null;
  riskLevel: string | null;
  status: string;
  approvedAt: string | null;
  rejectedAt: string | null;
  createdAt: string;
};

export type ApiResponse<T> = {
  timestamp: string;
  success: boolean;
  message: string;
  data: T;
};

export async function getPendingTasks(): Promise<TaskResponse[]> {
  const response = await apiClient.get<ApiResponse<TaskResponse[]>>(
    "/api/tasks/pending"
  );

  return response.data.data;
}

export async function approveTask(taskId: string): Promise<TaskResponse> {
  const response = await apiClient.post<ApiResponse<TaskResponse>>(
    `/api/tasks/${taskId}/approve`
  );

  return response.data.data;
}

export async function rejectTask(taskId: string): Promise<TaskResponse> {
  const response = await apiClient.post<ApiResponse<TaskResponse>>(
    `/api/tasks/${taskId}/reject`
  );

  return response.data.data;
}
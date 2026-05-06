import { apiClient } from "./apiClient";

export type AiChatResponse = {
  id: string;
  question: string;
  answer: string;
  status: string;
  provider: string;
  model: string;
  createdAt: string;
};

export type ApiResponse<T> = {
  timestamp: string;
  success: boolean;
  message: string;
  data: T;
};

export async function askAiQuestion(question: string): Promise<AiChatResponse> {
  const response = await apiClient.post<ApiResponse<AiChatResponse>>("/api/ai/chat", {
    question,
  });

  return response.data.data;
}
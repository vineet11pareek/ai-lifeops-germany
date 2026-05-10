import { apiClient } from "./apiClient";

export type DocumentAnalysisResponse = {
  id: string;
  title: string;
  content: string;
  summary: string | null;
  deadlineText: string | null;
  requiredAction: string | null;
  riskLevel: string | null;
  suggestedNextStep: string | null;
  status: string;
  createdAt: string;
};

export type ApiResponse<T> = {
  timestamp: string;
  success: boolean;
  message: string;
  data: T;
};

export async function analyzeDocument(
  title: string,
  content: string
): Promise<DocumentAnalysisResponse> {
  const response = await apiClient.post<ApiResponse<DocumentAnalysisResponse>>(
    "/api/documents/analyze",
    {
      title,
      content,
    }
  );

  return response.data.data;
}
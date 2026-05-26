import { apiClient } from "./apiClient";

export type TruthAnalysisResponse = {
  id: string;
  title: string;
  content: string;
  claimSummary: string | null;
  trustScore: number | null;
  riskLevel: string | null;
  explanation: string | null;
  suggestedVerificationSteps: string | null;
  status: string;
  createdAt: string;
};

export type ApiResponse<T> = {
  timestamp: string;
  success: boolean;
  message: string;
  data: T;
};

export async function analyzeTruth(
  title: string,
  content: string
): Promise<TruthAnalysisResponse> {
  const response = await apiClient.post<ApiResponse<TruthAnalysisResponse>>(
    "/api/truth/analyze",
    {
      title,
      content,
    }
  );

  return response.data.data;
}
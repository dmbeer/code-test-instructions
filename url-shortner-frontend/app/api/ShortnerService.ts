import axios from 'axios';

const serverUrl = import.meta.env.VITE_API_URL;

class ShortnerServiceAPI {

    async getShortURL(fullUrl: string, alias: String): Promise<ShortenURLResponse> {
        const response = await axios.post<ShortenURLResponse>(
            `${serverUrl}/shorten`,
            {
                fullUrl,
                customAlias: alias,
            },
            {
                headers: {
                    "Content-Type": "application/json",
                },
            }
        );
        return response.data;
    }

    async listURLS() :Promise<URLSResponse[]> {
        const response = await axios.get<URLSResponse[]>(`${serverUrl}/urls`);
        return response.data
    }
}

export default new ShortnerServiceAPI();
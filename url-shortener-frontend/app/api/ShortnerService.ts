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

    async listURLS({ signal }: { signal?: AbortSignal }) :Promise<URLSResponse[]> {
        const response = await axios.get<URLSResponse[]>(`${serverUrl}/urls`, {signal});
        return response.data
    }

    async deleteAlias(alias: string) {
        return await axios.delete(`${serverUrl}/${alias}`)
    }
}

export default new ShortnerServiceAPI();
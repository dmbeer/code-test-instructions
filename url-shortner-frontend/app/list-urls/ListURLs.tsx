import {Header} from "~/components/Header";
import {useEffect, useState} from "react";
import ShortnerService from "~/api/ShortnerService";
import axios from "axios";
import RowActions from "~/components/RowActions";

export function ListURLS() {

    const [urls, setUrls] = useState<URLSResponse[]>([]);

    async function getURLs() {
        try {
            const response = await ShortnerService.listURLS();
            setUrls(response);
        } catch (error) {
            if (axios.isAxiosError(error)) {
                console.error("API error:", error.response?.data);
                console.error("Status:", error.response?.status);
            } else {
                console.error("Unexpected error:", error);
            }
        }
    }

    useEffect(() => {
        if (urls.length === 0) {
            getURLs();
        }
    });

    return (
        <main className="flex flex-col items-center gap-16 min-h-0">
            <Header />
            <div className="flex flex-col md:flex-row justify-center items-start">
                <table className="w-full md:w-1/2">
                    <thead>
                    <tr>
                        <th className="x-4 py-2 text-left">Full Url</th>
                        <th className="px-4 py-2 text-left">Short URL</th>
                        <th className={"px-4 py-2 text-left"}>Alias</th>
                    </tr>
                    </thead>
                    <tbody>
                    {urls.map((url) => (
                        <tr key={url.alias}>
                            <td className="px-4 py-2 truncate max-w-xs">
                                <a href={url.fullUrl} target="_blank" rel="noreferrer">
                                    {url.fullUrl}
                                </a>
                            </td>
                            <td className="px-4 py-2 truncate max-w-xs">
                                <a href={url.fullUrl} target="_blank" rel="noreferrer">
                                    {url.shortUrl}
                                </a>
                            </td>
                            <td className="px-4 py-2 truncate max-w-xs">{url.alias}</td>
                            <td className="px-4 py-2">
                                <RowActions item={url} />
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </main>
    )
}
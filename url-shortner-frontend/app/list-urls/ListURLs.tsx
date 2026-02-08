import {Header} from "~/components/Header";
import {useEffect, useState} from "react";
import ShortnerService from "~/api/ShortnerService";
import axios from "axios";
import RowActions from "~/components/RowActions";
import ConfirmDeleteModal from "~/components/ConfirmDeleteModal";
import {Link} from "react-router";

export function ListURLS() {

    const [urls, setUrls] = useState<URLSResponse[]>([]);
    const [itemToDelete, setItemToDelete] =
        useState<URLSResponse | null>(null);

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

    async function handleDelete(alias: string) {
        try {
            const response = await ShortnerService.deleteAlias(alias);
            setItemToDelete(null)
            getURLs();
        } catch (error) {
            if (axios.isAxiosError(error)) {
                console.error("API error:", error.response?.data);
                console.error("Status:", error.response?.status);
            } else {
                console.error("Unexpected error:", error);
            }
        }
    }

    return (
        <main className="flex items-center justify-center pt-16 pb-4">
            <div className="flex-1 flex flex-col items-center gap-16 min-h-0">
                <Header />
                <div>
                    <Link className="px-4 mt-4 w-full rounded-lg bg-blue-600 py-2 font-semibold text-white hover:bg-blue-700 transition" to="/">
                        Home
                    </Link>
                </div>
                {urls.length > 0 ? (
                    <div className="flex flex-col md:flex-row justify-center items-start">
                        <table className="min-w-full border border-gray-300 border-collapse">
                            <thead className="bg-black border-b border-gray-300">
                            <tr>
                                <th className="x-4 py-2 text-left text-red-400">Full Url</th>
                                <th className="px-4 py-2 text-left text-red-400">Short URL</th>
                                <th className="px-4 py-2 text-left text-red-400">Alias</th>
                                <th className="px-4 py-2 text-left text-red-400"></th>
                            </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-200">
                            {urls.map((url) => (
                                <tr key={url.alias}>
                                    <td className="py-2 max-w-xs truncate">
                                        <a href={url.fullUrl} target="_blank" rel="noreferrer">
                                            {url.fullUrl}
                                        </a>
                                    </td>
                                    <td className="px-4 py-2 max-w-xs">
                                        <a href={url.fullUrl} target="_blank" rel="noreferrer">
                                            {url.shortUrl}
                                        </a>
                                    </td>
                                    <td className="px-4 py-2 max-w-xs">{url.alias}</td>
                                    <td className="px-4 py-2">
                                        <RowActions item={url} onRequestDelete={setItemToDelete} />
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                ):
                <div className="flex flex-col items-center justify-center pt-16">
                    <h3>There are No URLS to get. Please request a URL to be shortened by the Home page.</h3>
                </div>
                }
                {itemToDelete && (
                    <ConfirmDeleteModal
                        item={itemToDelete}
                        onCancel={() => setItemToDelete(null)}
                        onConfirm={async () => {
                            await handleDelete(itemToDelete?.alias);
                            setItemToDelete(null);
                        }}
                    />
                )}
            </div>
        </main>
    )
}
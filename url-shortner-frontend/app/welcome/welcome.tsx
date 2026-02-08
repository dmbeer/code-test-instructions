import {useState} from "react";

export function Welcome() {
    const [url, setUrl] = useState("");

    return (
        <main className="flex items-center justify-center pt-16 pb-4">
            <div className="flex-1 flex flex-col items-center gap-16 min-h-0">
                <header className="flex flex-col items-center gap-9">
                    <div className="w-[500px] max-w-[100vw] p-4">
                        <h1 className="text-3xl font-bold text-4xl leading-tight">URL Shortener</h1>
                        <h3>What to shorten a long URL</h3>
                    </div>
                </header>
                <div className="flex flex-col md:flex-row gap-8 justify-center items-start">
                    <div className="w-full md:w-96 rounded-xl border border-gray-700 p-6 text-center">
                        <ul>
                            {resources.map(({text, icon}) => (
                                <li>
                                    <a
                                        className="group flex items-center gap-3 self-stretch p-3 leading-normal text-blue-700 hover:underline dark:text-blue-500"
                                        target="_blank"
                                        rel="noreferrer"
                                    >
                                        {icon}
                                        {text}
                                    </a>
                                </li>
                            ))}
                        </ul>
                    </div>
                    <div className="w-full md:w-96 rounded-xl border border-gray-700 p-6 text-center">
                        <div className="flex flex-col gap-4">
                            <input
                                type="text"
                                placeholder="Paste your long URL here..."
                                value={url}
                                onChange={(e) => setUrl(e.target.value)}
                                className="w-full rounded-lg bg-gray-900 border border-gray-600 px-10 py-2 text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500"
                            />
                            <input
                                type="text"
                                placeholder="Paste your long URL here..."
                                value={url}
                                onChange={(e) => setUrl(e.target.value)}
                                className="w-full rounded-lg bg-gray-900 border border-gray-600 px-10 py-2 text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500"
                            />
                        </div>
                        <button className="mt-4 w-full rounded-lg bg-blue-600 py-2 font-semibold text-white hover:bg-blue-700 transition">
                            Shorten URL
                        </button>
                    </div>
                </div>
            </div>
        </main>
    );
}

const resources = [
    {
        text: "Get a Shortened URL in the box below",
        icon: (
            <svg
                xmlns="http://www.w3.org/2000/svg"
                width="24"
                height="20"
                viewBox="0 0 20 20"
                fill="none"
                className="stroke-gray-600 group-hover:stroke-current dark:stroke-gray-300"
            >
                <path
                    d="M9.99981 10.0751V9.99992M17.4688 17.4688C15.889 19.0485 11.2645 16.9853 7.13958 12.8604C3.01467 8.73546 0.951405 4.11091 2.53116 2.53116C4.11091 0.951405 8.73546 3.01467 12.8604 7.13958C16.9853 11.2645 19.0485 15.889 17.4688 17.4688ZM2.53132 17.4688C0.951566 15.8891 3.01483 11.2645 7.13974 7.13963C11.2647 3.01471 15.8892 0.951453 17.469 2.53121C19.0487 4.11096 16.9854 8.73551 12.8605 12.8604C8.73562 16.9853 4.11107 19.0486 2.53132 17.4688Z"
                    strokeWidth="1.5"
                    strokeLinecap="round"
                />
            </svg>
        ),
    },
    {
        text: "Provide a Custom Alias",
        icon: (
            <svg
                xmlns="http://www.w3.org/2000/svg"
                width="24"
                height="20"
                viewBox="0 0 20 20"
                fill="none"
                className="stroke-gray-600 group-hover:stroke-current dark:stroke-gray-300"
            >
                <path
                    d="M9.99981 10.0751V9.99992M17.4688 17.4688C15.889 19.0485 11.2645 16.9853 7.13958 12.8604C3.01467 8.73546 0.951405 4.11091 2.53116 2.53116C4.11091 0.951405 8.73546 3.01467 12.8604 7.13958C16.9853 11.2645 19.0485 15.889 17.4688 17.4688ZM2.53132 17.4688C0.951566 15.8891 3.01483 11.2645 7.13974 7.13963C11.2647 3.01471 15.8892 0.951453 17.469 2.53121C19.0487 4.11096 16.9854 8.73551 12.8605 12.8604C8.73562 16.9853 4.11107 19.0486 2.53132 17.4688Z"
                    strokeWidth="1.5"
                    strokeLinecap="round"
                />
            </svg>
        ),
    },
];

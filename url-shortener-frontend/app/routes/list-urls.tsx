import type { Route } from "./+types/home";
import { Welcome } from "~/welcome/welcome";
import {ListURLS} from "~/list-urls/ListURLs";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "URL Shortener", component: Welcome },
        { name: "description", content: "Welcome to URL Shortener!" },
    ];
}

export default function ListUrls() {
    return <ListURLS />;
}

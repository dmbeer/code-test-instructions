import {type RouteConfig, index, route} from "@react-router/dev/routes";

export default [index("routes/home.tsx"), route("list-urls", "routes/list-urls.tsx")] satisfies RouteConfig;

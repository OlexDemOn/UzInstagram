import { getPost } from "@/api/endpoints";
import Post from "@/components/post";
import { createFileRoute } from "@tanstack/react-router";
import { useState } from "react";
import { TPost } from "../../types/post";

export const Route = createFileRoute("/_auth/")({
    component: () => <App />,
});

function App() {
    const [posts, setPosts] = useState<TPost[]>([
        {
            id: "0",
            title: "Cute cats",
            img_url: "asdasdsa",
            body: "Cute cats are cute",
        },
        {
            id: "1",
            title: "Cute dogs",
            img_url: "asdasdsa",
            body: "Cute dogs are cute",
        },
        {
            id: "2",
            title: "Cute birds",
            img_url: "asdasdsa",
            body: "Cute birds are cute",
        },
        {
            id: "3",
            title: "Cute fish",
            img_url: "asdasdsa",
            body: "Cute fish are cute",
        },
        {
            id: "4",
            title: "Cute lizards",
            img_url: "asdasdsa",
            body: "Cute lizards are cute",
        },
    ]);

    const resp = getPost();

    resp.then((data) => {
        setPosts(data);
    });

    return (
        <div>
            <h1>Posts</h1>
            <div className="flex flex-col items-center gap-y-8">
                {posts.map((post: TPost) => (
                    <Post post={post} />
                ))}
            </div>
        </div>
    );
}

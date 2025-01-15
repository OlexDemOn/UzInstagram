import { createFileRoute } from "@tanstack/react-router";
import { useAuth } from "../../providers/AuthProvider";
import { useEffect, useState } from "react";
import { TPost } from "types/post";
import axios from "axios";
import {
    AvatarContainer,
    ImageContainer,
    PostContainer,
} from "@/components/post";

export const Route = createFileRoute("/_auth/saved")({
    component: () => <Saved />,
});

const Saved = () => {
    const [posts, setPosts] = useState<TPost[]>([]);

    const auth = useAuth();
    const username = auth.user?.username || "";

    useEffect(() => {
        const ids = JSON.parse(localStorage.getItem("bookmarks") || "[]");
        const fetchPosts = async () => {
            try {
                const response = await axios.post(
                    "http://localhost:5000/api/posts/byId",
                    ids,
                    {
                        headers: {
                            Authorization: username,
                        },
                    }
                );
                setPosts(response.data.data);
            } catch (error) {
                console.error("Error fetching posts:", error);
            }
        };
        fetchPosts();
    }, []);

    return (
        <div className="grid grid-cols-1 gap-5 md:grid-cols-2 lg:grid-cols-3">
            {posts.map((post) => (
                <PostContainer key={post.id} post={post}>
                    <div className="flex items-center gap-2">
                        <AvatarContainer
                            imgUrl={post.profileImageUrl}
                            username={post.username}
                        />
                        {post.username}
                    </div>
                    <ImageContainer post={post} />
                </PostContainer>
            ))}
        </div>
    );
};

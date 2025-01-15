import {
    AvatarContainer,
    ImageContainer,
    PostContainer,
} from "@/components/post";
import { createFileRoute } from "@tanstack/react-router";
import axios from "axios";
import { useAuth } from "../../providers/AuthProvider";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { TPost } from "types/post";

export const Route = createFileRoute("/_auth/topTen")({
    component: () => <TopTen />,
});

const TopTen = () => {
    const [posts, setPosts] = useState<TPost[]>([]);
    const [loading, setLoading] = useState(false);
    const [hasMore, setHasMore] = useState(true);
    const { t } = useTranslation();

    const auth = useAuth();
    const username = auth.user?.username || "";

    const fetchPosts = async () => {
        if (loading || !hasMore) return;

        setLoading(true);
        try {
            const response = await axios.get(
                `http://localhost:5000/api/posts/top10`,
                {
                    headers: {
                        Authorization: username,
                    },
                }
            );
            setPosts((prev) => [...prev, ...response.data.data]);
            setHasMore(response.data.data.hasMorePosts);
        } catch (error) {
            console.error("Error fetching posts:", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchPosts();
    }, []);

    return (
        <div className="flex flex-col items-center gap-5 max-w-[600px] mx-auto">
            {posts.map((post: TPost) => (
                <PostContainer key={post.id} post={post}>
                    <div className="flex items-center gap-2">
                        <AvatarContainer
                            username={post.username}
                            imgUrl={post.profileImageUrl}
                        />
                        <p>{post.username}</p>
                    </div>
                    <ImageContainer post={post} />
                </PostContainer>
            ))}
            {loading && <p>{t("loading")}</p>}
            {!hasMore && <p>No more posts to show.</p>}
        </div>
    );
};

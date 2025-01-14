import { createFileRoute } from "@tanstack/react-router";
import axios from "axios";
import { useEffect, useState } from "react";
import { animateScroll as scroll } from "react-scroll";
import { Button } from "@/components/ui/button";
import {
    PostContainer,
    AvatarContainer,
    ImageContainer,
} from "@/components/post";
import { useAuth } from "../../providers/AuthProvider";
import type { TPost } from "types/post";

export const Route = createFileRoute("/_auth/")({
    component: () => <Index />,
});

const Index = () => {
    const [posts, setPosts] = useState<TPost[]>([]);
    const [page, setPage] = useState(0);
    const [loading, setLoading] = useState(false);
    const [hasMore, setHasMore] = useState(true);
    const [isVisible, setIsVisible] = useState(false);

    const auth = useAuth();
    const username = auth.user?.username || "";

    const fetchPosts = async () => {
        if (loading || !hasMore) return;

        setLoading(true);
        try {
            const response = await axios.get(
                `http://localhost:5000/api/posts?page=${page}`,
                {
                    headers: {
                        Authorization: username,
                    },
                }
            );
            console.log(response);
            setPosts((prev) => [...prev, ...response.data.data.posts]);
            setHasMore(response.data.data.hasMorePosts);
            console.log(response.data.data);
        } catch (error) {
            console.error("Error fetching posts:", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchPosts();
    }, [page]);

    const handleScroll = () => {
        if (
            window.innerHeight + document.documentElement.scrollTop >=
            document.documentElement.offsetHeight - 200
        ) {
            console.log("fetching more posts");
            setPage((prev) => prev + 1);
        }
        if (window.scrollY > 200) {
            setIsVisible(true);
        } else {
            setIsVisible(false);
        }
    };

    useEffect(() => {
        window.addEventListener("scroll", handleScroll);
        return () => window.removeEventListener("scroll", handleScroll);
    }, []);

    const scrollToTop = () => {
        scroll.scrollToTop({ duration: 500, smooth: true });
    };

    return (
        <div className="flex flex-col items-center gap-5 max-w-[600px] mx-auto">
            {posts.map((post: TPost) => (
                <PostContainer key={post.id} post={post}>
                    <div className="flex items-center gap-2">
                        <AvatarContainer post={post} />
                        <p>{post.username}</p>
                    </div>
                    <ImageContainer post={post} />
                </PostContainer>
            ))}
            {loading && <p>Loading...</p>}
            {!hasMore && <p>No more posts to show.</p>}
            <div>
                {isVisible && (
                    <Button
                        onClick={scrollToTop}
                        className="fixed bottom-4 right-4 bg-primary text-white rounded-full shadow-lg hover:bg-primary-dark transition-all text-2xl"
                    >
                        ↑
                    </Button>
                )}
            </div>
        </div>
    );
};

export default Index;

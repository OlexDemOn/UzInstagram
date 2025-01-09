import { Card } from "@/components/ui/card";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { createFileRoute } from "@tanstack/react-router";
import axios from "axios";
import { useEffect, useState } from "react";
import { animateScroll as scroll } from "react-scroll";
import { Button } from "@/components/ui/button";

export const Route = createFileRoute("/_auth/")({
    component: () => <Index />,
});

const Index = () => {
    const [posts, setPosts] = useState([]);
    const [page, setPage] = useState(0);
    const [loading, setLoading] = useState(false);
    const [hasMore, setHasMore] = useState(true);
    const [isVisible, setIsVisible] = useState(false);

    const fetchPosts = async () => {
        if (loading || !hasMore) return;

        setLoading(true);
        try {
            const response = await axios.get(
                `http://localhost:5000/api/posts?page=${page}`
            );
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
        <div className="flex flex-col items-center gap-5">
            {posts.map((post, index) => (
                <Card key={index} className="min-w-60 w-[500px] min-h-60 p-5">
                    <div className="flex items-center gap-5">
                        <Avatar>
                            <AvatarImage src={post.profileImageUrl} />
                            <AvatarFallback>{post.username}</AvatarFallback>
                        </Avatar>
                        <p>{post.username}</p>
                    </div>
                    <p>{post.title}</p>
                    {post.imageUrl !== "" ? (
                        <img src={post.imageUrl} alt="Post" />
                    ) : (
                        <>{post.description}</>
                    )}
                </Card>
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

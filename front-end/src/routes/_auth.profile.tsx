import {
    AvatarContainer,
    ImageContainer,
    PostContainer,
} from "@/components/post";
import { createFileRoute } from "@tanstack/react-router";
import axios from "axios";
import { useAuth } from "../../providers/AuthProvider";
import { useEffect, useState } from "react";

export const Route = createFileRoute("/_auth/profile")({
    component: () => <Index />,
});

const Index = () => {
    const [loading, setLoading] = useState(true);
    const [data, setData] = useState([]);

    const auth = useAuth();
    const username = auth.user?.username || "";

    const fetchPosts = async () => {
        setLoading(true);
        try {
            const response = await axios.get(
                `http://localhost:5000/api/posts/user?username=${username}`
            );
            setData(response.data.data);
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
        <div className="max-w-[600px] mx-auto">
            {loading ? (
                <p>Loading...</p>
            ) : (
                data && (
                    <div>
                        <div className="flex items-center gap-5">
                            <AvatarContainer post={data.profile} />
                            <div>
                                <p>{data.profile.username}</p>
                                <p>{data.profile.fullName}</p>
                            </div>
                            <div>
                                <p>Posts: {data.posts.length}</p>
                                <p>
                                    {/* Followers: {data.profile.followers.length} */}
                                </p>
                                <p>
                                    {/* Following: {data.profile.following.length} */}
                                </p>
                            </div>
                        </div>
                        <p>{data.profile.bio}</p>
                        <div className="flex flex-col items-center gap-5 ">
                            {data.posts.map((post, index) => (
                                <PostContainer key={index} post={post}>
                                    <ImageContainer post={post} />
                                </PostContainer>
                            ))}
                        </div>
                    </div>
                )
            )}
        </div>
    );
};

export default Index;

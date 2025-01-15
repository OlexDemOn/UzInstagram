import {
    AvatarContainer,
    ImageContainer,
    PostContainer,
} from "@/components/post";
import { createFileRoute, Link } from "@tanstack/react-router";
import axios from "axios";
import { useAuth } from "../../providers/AuthProvider";
import { useEffect, useState } from "react";
import type { TProfile } from "types/users";
import type { TPost } from "types/post";
import { Button } from "@/components/ui/button";

export const Route = createFileRoute("/_auth/profile/$username")({
    component: () => <Index />,
});

type TProfileData = {
    profile: TProfile;
    posts: TPost[];
};

const Index = () => {
    const [loading, setLoading] = useState(true);
    const [data, setData] = useState<TProfileData>();
    const [followers, setFollowers] = useState(0);
    const [following, setFollowing] = useState(0);
    const { username } = Route.useParams();
    const auth = useAuth();
    const currentUsername = auth.user?.username || "";

    const fetchPosts = async () => {
        setLoading(true);
        try {
            const response = await axios.get(
                `http://localhost:5000/api/posts/user?username=${username}`,
                {
                    headers: {
                        Authorization: currentUsername,
                    },
                }
            );
            setData(response.data.data);
            setFollowers(response.data.data.profile.followers);
            setFollowing(response.data.data.profile.following);
        } catch (error) {
            console.error("Error fetching posts:", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchPosts();
    }, []);

    console.log(data);
    return (
        <div className="max-w-[600px] mx-auto">
            {loading ? (
                <p>Loading...</p>
            ) : (
                data && (
                    <div>
                        <div className="flex flex-col gap-5 mb-5">
                            <div className="flex justify-between gap-5">
                                <AvatarContainer
                                    imgUrl={data.profile.profileImageUrl}
                                />
                                <div className="flex flex-col items-center gap-2">
                                    <p className="font-bold text-xl">
                                        {" "}
                                        {data.posts.length}
                                    </p>
                                    <p>Posts</p>
                                </div>
                                <div className="flex flex-col items-center gap-2">
                                    <p className="font-bold text-xl">
                                        {" "}
                                        {followers}
                                    </p>
                                    <p>followers</p>
                                </div>
                                <div className="flex flex-col items-center gap-2">
                                    <p className="font-bold text-xl">
                                        {" "}
                                        {following}
                                    </p>
                                    <p>following</p>
                                </div>
                            </div>
                            <div>
                                <p className="font-bold text-xl">
                                    {data.profile.username}
                                </p>
                                <p className="mb-2">{data.profile.fullName}</p>
                                <p>{data.profile.bio}</p>
                            </div>
                            <ControlPanel
                                currentUsername={currentUsername}
                                username={username}
                                setFollowers={setFollowers}
                                setFollowing={setFollowing}
                            />
                        </div>
                        <div className="flex flex-col items-center gap-5 ">
                            {data.posts.map((post) => (
                                <PostContainer key={post.id} post={post}>
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

const ControlPanel = ({
    currentUsername,
    username,
    setFollowers,
    setFollowing,
}: {
    currentUsername: string;
    username: string;
    setFollowers: React.Dispatch<React.SetStateAction<number>>;
    setFollowing: React.Dispatch<React.SetStateAction<number>>;
}) => {
    const [isFollowing, setIsFollowing] = useState(false);

    const followUser = async () => {
        try {
            let link = `follow`;
            if (isFollowing) {
                link = `unfollow`;
            }

            const response = await axios.post(
                `http://localhost:5000/api/users/${currentUsername}/${link}/${username}`
            );
            if (isFollowing) {
                setFollowers((prev) => parseInt(prev) - 1);
            } else {
                setFollowers((prev) => parseInt(prev) + 1);
            }
            setIsFollowing(!isFollowing);
            console.log(response);
        } catch (error) {
            console.error("Error following user:", error);
        }
    };

    const isUserFollowProfile = async () => {
        try {
            const response = await axios.get(
                `http://localhost:5000/api/users/${currentUsername}/follows/${username}`
            );
            setIsFollowing(response.data);
        } catch (error) {
            console.error("Error following user:", error);
        }
    };

    useEffect(() => {
        isUserFollowProfile();
    }, []);
    console.log(isFollowing);
    return (
        <div>
            {currentUsername === username ? (
                <Link to="/profileinfo">
                    <Button>Edit Profile</Button>
                </Link>
            ) : (
                <Button
                    onClick={followUser}
                    variant={isFollowing ? "secondary" : "default"}
                >
                    {!isFollowing ? "Follow" : "Unfollow"}
                </Button>
            )}
        </div>
    );
};

export default Index;

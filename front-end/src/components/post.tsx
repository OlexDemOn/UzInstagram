import { Card } from "@/components/ui/card";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import {
    ComponentPropsWithoutRef,
    ElementRef,
    forwardRef,
    useState,
} from "react";
import {
    Collapsible,
    CollapsibleContent,
    CollapsibleTrigger,
} from "@/components/ui/collapsible";

import { FiMessageCircle } from "react-icons/fi";
import { FaHeart, FaRegHeart } from "react-icons/fa";
import { TPost } from "types/post";
import axios from "axios";
import { useAuth } from "../../providers/AuthProvider";

export const PostContainer = forwardRef<
    ElementRef<"div">,
    ComponentPropsWithoutRef<"div"> & {
        post: TPost;
    }
>(({ post, children, className, ...props }, ref) => {
    const auth = useAuth();
    const username = auth.user?.username || "";

    return (
        <Card
            className={`min-h-60 w-full flex flex-col gap-5 p-5 ${className}`}
            ref={ref}
            {...props}
        >
            {children}
            <Collapsible>
                <div className="flex items-center gap-2">
                    <Likes post={post} username={username} />
                    <CollapsibleTrigger>
                        <FiMessageCircle size={24} />
                    </CollapsibleTrigger>
                </div>
                <Comments post={post} />
                {/* </div> */}
            </Collapsible>
        </Card>
    );
});

export const AvatarContainer = forwardRef<
    ElementRef<"div">,
    ComponentPropsWithoutRef<"div"> & {
        post: TPost;
    }
>(({ post, className, ...props }, ref) => {
    return (
        <Avatar className={className} {...props} ref={ref}>
            <AvatarImage className="bg-cover" src={post.profileImageUrl} />
            <AvatarFallback>{post.username[0]}</AvatarFallback>
        </Avatar>
    );
});

export const ImageContainer = forwardRef<
    ElementRef<"div">,
    ComponentPropsWithoutRef<"div"> & {
        post: TPost;
    }
>(({ post, className, ...props }, ref) => {
    return (
        <div
            className={`flex flex-col gap-2 ${className}`}
            {...props}
            ref={ref}
        >
            <p>{post.title}</p>
            {post.imageUrl !== "" ? (
                <img src={post.imageUrl} alt="Post" />
            ) : (
                <>{post.description}</>
            )}
        </div>
    );
});

export const Likes = forwardRef<
    ElementRef<"div">,
    ComponentPropsWithoutRef<"div"> & {
        post: TPost;
        username: string;
    }
>(({ post, username, className, ...props }, ref) => {
    const [liked, setLiked] = useState(post.likedByCurrentUser);
    const [likesCount, setLikesCount] = useState<number>(post.likesCount);
    const likePost = () => {
        let link = "http://localhost:5000/api/posts/" + post.id;
        if (liked) {
            link += "/unlike";
        } else {
            link += "/like";
        }
        axios
            .post(
                link,
                {},
                {
                    headers: {
                        Authorization: `${username}`,
                    },
                }
            )
            .then((response) => {
                if (liked) {
                    setLikesCount((prev) => prev - 1);
                    setLiked(false);
                } else {
                    setLikesCount((prev) => prev + 1);
                    setLiked(true);
                }
            })
            .catch((error) => {
                console.error("Error liking post:", error);
            });
    };

    return (
        <div
            className={`flex items-center w-fit gap-2 hover:cursor-pointer size-12 ${className}`}
            onClick={likePost}
            ref={ref}
            {...props}
        >
            {likesCount ? <span>{likesCount}</span> : 0}
            {liked ? (
                <FaHeart size={24} color="red" />
            ) : (
                <FaRegHeart size={24} />
            )}
        </div>
    );
});

export const Comments = forwardRef<
    ElementRef<"div">,
    ComponentPropsWithoutRef<"div"> & {
        post: TPost;
    }
>(({ post, className, ...props }, ref) => {
    return (
        <CollapsibleContent>
            Yes. Free to use for personal and commercial projects. No
            attribution required.
        </CollapsibleContent>
    );
});

import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { useForm } from "react-hook-form";
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useAuth } from "../../providers/AuthProvider";
import { useEffect, useState } from "react";
import { Separator } from "@/components/ui/separator";
import { getProfile } from "@/api/endpoints";
import { toast } from "react-toastify";
import axios from "axios";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { UserProfile } from "types/users";

export const Route = createFileRoute("/_auth/profileInfo")({
    component: UserInfo,
});

function UserInfo() {
    const auth = useAuth();
    const username = auth.user?.username || "";
    const [userProfile, setUserProfile] = useState<UserProfile>();

    useEffect(() => {
        const test = async () => {
            const response = await getProfile(username);
            setUserProfile(response);
        };
        test();
    }, []);

    return (
        <div className="flex flex-1 justify-center items-center w-full gap-5">
            <Card className="w-80">
                <CardHeader>
                    <CardTitle>Give more information about you</CardTitle>
                </CardHeader>
                <CardContent>
                    <CardTitle>Username: </CardTitle>
                    {username}
                </CardContent>
                <CardContent>
                    {userProfile && (
                        <FormContainer
                            userProfile={userProfile}
                            setUserProfile={setUserProfile}
                        />
                    )}
                </CardContent>
            </Card>
        </div>
    );
}

const formSchema = z.object({
    fullName: z.string().min(4),
    profileImg: z.instanceof(File).optional(),
    bio: z.string(),
});

const FormContainer = ({
    userProfile,
    setUserProfile,
}: {
    userProfile: UserProfile;
    setUserProfile;
}) => {
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            fullName: "",
            profileImg: null,
            bio: "",
        },
    });

    useEffect(() => {
        const test = async () => {
            const response = await getProfile(userProfile.username);
            form.setValue("fullName", response.fullName);
            form.setValue("bio", response.bio);
            // form.setValue("profileImg", response.profile_image_url);
        };
        test();
    }, []);

    async function onSubmit(values: z.infer<typeof formSchema>) {
        // const { fullName, bio, profileImg } = values;
        try {
            await axios.put("http://localhost:5000/api/user/profile", values, {
                headers: {
                    "Content-Type": "multipart/form-data",
                    Authorization: userProfile.username,
                },
            });
            toast.success("Your profile has been updated");
            setUserProfile((userProfile: UserProfile) => {
                return {
                    ...userProfile,
                    bio: values.bio,
                    profileImg: URL.createObjectURL(values.profileImg),
                    fullName: values.fullName,
                };
            });
        } catch (error) {
            setError(error.message);
        }
    }

    return (
        <Form {...form}>
            <form
                onSubmit={form.handleSubmit(onSubmit)}
                className="flex flex-col gap-4"
            >
                <FormField
                    control={form.control}
                    name="profileImg"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Profile img</FormLabel>
                            <div className="flex gap-5">
                                <Avatar>
                                    <AvatarImage src={userProfile.profileImg} />
                                    <AvatarFallback>
                                        {userProfile.fullName[0] ||
                                            userProfile.username[0]}
                                    </AvatarFallback>
                                </Avatar>
                                <FormControl>
                                    <Input
                                        className="cursor-pointer"
                                        type="file"
                                        onChange={(e) =>
                                            field.onChange(e.target.files[0])
                                        }
                                    />
                                </FormControl>
                                <FormMessage />
                            </div>
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="fullName"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Full name</FormLabel>
                            <FormControl>
                                <Input {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />

                <FormField
                    control={form.control}
                    name="bio"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Bio</FormLabel>
                            <FormControl>
                                <Input {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                {error && <FormMessage type="error">{error}</FormMessage>}
                <div className="flex justify-between h-10 items-center">
                    <Button
                        variant="ghost"
                        type="submit"
                        onClick={() => navigate({ to: "/" })}
                    >
                        Skip
                    </Button>
                    <Separator orientation="vertical" />
                    <Button type="submit">Update</Button>
                </div>
            </form>
        </Form>
    );
};

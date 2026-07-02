import { GitBranch } from "lucide-react";
import { GitFork } from "lucide-react";

import { FolderGit } from "lucide-react";
import { Link } from "lucide-react";
import { Globe } from "lucide-react";

import { ExternalLink } from "lucide-react"; 

import SheetCard from "../components/SheetCard";

const recommendedData = [
    { id: 1, title: "Nocturne Op. 9 No. 2", author: "Frédéric Chopin", imageUrl: "https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcSPWHImQJ8sSGLC7bjRMQxXYs21yOoVa88FxYZywEO8_r1H9XSIDYhLBgLSw4q8x9VD3nd6eZikYhub-d3uU2W6sb4reXtd&usqp=CAc", category: "Public Domain", views: "12k" },
    { id: 2, title: "Waltz Cis-Moll", author: "Norbert Dominkiewicz", imageUrl: "https://encrypted-tbn2.gstatic.com/shopping?q=tbn:ANd9GcTBaDZhsH2MYK6FGRn_nqG3L230tXHPXBUHKCYlebe0h4uaT5u8PAhZ92gwGmznmat0eVZCHtT7hg8OFjGSeO09gvPEwXpvF9TrTQE8qZVcB4ZrRw8-F0iG5w&usqp=CAc", category: "Original", views: "850" },
    { id: 3, title: "Moonlight Sonata", author: "L. van Beethoven", imageUrl: "https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcRKpem5RN8Z5iKS7Z_sRy0UvqcX5dpupFHPpvVAV5xrNJ_07nPcDzW_0yDjE4ABoOz-zFTXttJaie56doTzbFSHKU47whdpWQ&usqp=CAc", category: "Public Domain", views: "25k" },
    { id: 4, title: "Jazz Improvisation #1", author: "Jan Kowalski", imageUrl: "https://encrypted-tbn0.gstatic.com/shopping?q=tbn:ANd9GcSHvRJvMp5kD9ZKfXaHeyBZhaeD-_3LmE0SlAXXE3t0i1Hrlk-OoXVUEphBURf_aQAud60qkf9sTaUTtGawyoWM2lpjHnre7fQkhP-amhOKSa7nAZ7TBS0k&usqp=CAc", category: "Original", views: "1.2k" },
    { id: 5, title: "Clair de Lune", author: "Debussy", imageUrl: "https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcRHhY0QB_0_Grmf3DfLVHgrb1itjTBH6DwiyS-ZtzzFQcAB33aat-6_JT6m9lCGke4tdSmddprwZJZDK0jwjDa3LoA_MFcbmg&usqp=CAc", category: "Public Domain", views: "99.2k" },
];


function Home() {
    return(
        <div className={`flex flex-col gap-8`}>
            <div className={`flex flex-row`}>
            <div className={`flex-2 flex flex-col`}>
                <h1 className={`font-bold text-3xl border-b w-fit pb-3`}>
                    Overview
                </h1>
                <div className={`flex flex-col justify-between gap-10 p-8`}>
                    <span className={`flex gap-2`}>
                        <GitFork />
                        MUSE is an open-source music notation software editor built as a diploma thesis project.
                    </span>
                    <span className={`flex gap-2`}>
                        <GitFork />
                        Scores are stored in the industry-standard .musicxml format, keeping your work portable and compatible
                        with other notation tools
                    </span>
                    <span className={`flex gap-2`}>
                        <GitFork />
                        Create and edit your projects alone or with your friends as it allows to write music in parallel way
                    </span>
                    <span className={`flex gap-2`}>
                        <GitFork />
                        Stack used in order to make it all happen: JavaFX, Spring Boot, React, PostgreSQL, TailwindCSS
                    </span>
                </div>
            </div>
            <div className={`flex-1 flex flex-col`}>
                <h1 className={`font-bold text-3xl border-b w-fit pb-3`}>
                    Author
                </h1>
                <div className={`flex flex-col justify-between gap-4 py-10`}>
                    <span>Project created by <span className={`font-bold`}>Norbert Dominkiewicz</span></span>
                    <span>Find me here:</span>
                    <div className={`px-4 py-2 flex flex-col gap-3`}>
                        <p className={`flex gap-2 w-32 justify-between`}><FolderGit /> GitHub: <ExternalLink /></p>
                        <p className={`flex gap-2 w-32 justify-between`}><Link /> LinkedIn: <ExternalLink /></p>
                        <p className={`flex gap-2 w-32 justify-between`}><Globe /> Website: <ExternalLink /></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    );
}

export default Home;